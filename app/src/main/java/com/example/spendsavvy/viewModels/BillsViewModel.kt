package com.example.spendsavvy.viewModels

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendsavvy.R
import com.example.spendsavvy.db.DatabaseHelper
import com.example.spendsavvy.models.Bills
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.repo.FirestoreRepository
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class BillsViewModel(
    context: Context,
    isOnline: Boolean,
    userId: String,
    private val notificationViewModel: NotificationViewModel,
    transactionViewModel: OverviewViewModel
): ViewModel() {

    private val transactionViewModel = transactionViewModel
    private val firestoreRepository = FirestoreRepository()
    private val dbHelper = DatabaseHelper(context)
    private val internet = isOnline
    private val currentContext = context
    private val currentUserId = userId
    val isLoading = MutableLiveData<Boolean>()

    val allBillsList = MutableLiveData<List<Bills>>() // All bills List
    val upcomingBillsList = MutableLiveData<List<Bills>>() // upcoming bills List
    val overdueBillsList = MutableLiveData<List<Bills>>() // overdue bills List
    val paidBillsList = MutableLiveData<List<Bills>>() // paid bills List
    val totalUpcomingBills = MutableLiveData<Int>()
    val totalOverdueBills = MutableLiveData<Int>()
    val totalPaidBills = MutableLiveData<Int>()


    private val sharedPref: SharedPreferences by lazy {
        context.getSharedPreferences("notification", Context.MODE_PRIVATE)
    }

    val isNotificationEnabled = MutableLiveData<Boolean>()

    fun updateNotificationStatus(isEnabled: Boolean) {
        isNotificationEnabled.value = isEnabled
        // Update notification status in NotificationViewModel
        notificationViewModel.isNotificationEnabled(isEnabled)
    }

    init {
        createNotificationChannel(context = currentContext)
        isNotificationEnabled.value = sharedPref.getBoolean("notificationEnabled", true)
        getBillsRecord()
    }

    fun getBillsRecord() {
        viewModelScope.launch {
            val billsFromDB: List<Bills>

            isLoading.value = true
            try {
                billsFromDB = if (internet) {
                    firestoreRepository.readItemsFromDatabase(
                        currentUserId,
                        "Bills",
                        Bills::class.java
                    )
                } else {
                    dbHelper.readBills(userId = currentUserId)
                }

                updateBills(billsFromDB)

            } catch (e: Exception) {
                Log.e(TAG, "Error getting bills", e)
            } finally {
                isLoading.postValue(false)
            }
        }
    }
    private suspend fun getBillId(bills: Bills): String {
        return firestoreRepository.getDocumentId("Bills", currentUserId, bills)
    }
    private suspend fun getCategoryId(category: Category): String {
        return firestoreRepository.getDocumentId("Categories", currentUserId, category)
    }

    private fun updateBills(bills: List<Bills>) {
        viewModelScope.launch {

            val upcomingBills = bills.filter { it.billsStatus == "UPCOMING" }
            val overdueBills = bills.filter { it.billsStatus == "OVERDUE" }
            val paidBills = bills.filter { it.billsStatus == "PAID" }

            allBillsList.postValue(bills)
            upcomingBillsList.postValue(upcomingBills)
            overdueBillsList.postValue(overdueBills)
            paidBillsList.postValue(paidBills)

            totalUpcomingBills.postValue(upcomingBills.size)
            totalOverdueBills.postValue(overdueBills.size)
            totalPaidBills.postValue(paidBills.size)

        }
    }
    fun addBillsToFirestore(
        bills: Bills,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val categoryId: String = getCategoryId(bills.category)

                firestoreRepository.addItem(
                    currentUserId,
                    "Bills",
                    bills,
                    "B%04d",
                    onSuccess = { documentId ->
                        dbHelper.addNewBill(
                            billId = documentId,
                            amount = bills.amount,
                            categoryId = categoryId,
                            description = bills.description,
                            selectedDueDate = bills.selectedDueDate,
                            billsStatus = bills.billsStatus,
                            userId = currentUserId
                        )
                        val currentBills = allBillsList.value ?: emptyList()
                        val updatedBills = currentBills + bills
                        updateBills(updatedBills)
                        onSuccess()
                        println("send notification: ${isNotificationEnabled.value}")
                        if (isNotificationEnabled.value == true) {
                            createBillMadeNotification(bills)
                        }
                    },
                    onFailure = { exception ->
                        Log.e(TAG, "Error adding bills", exception)
                        onFailure(exception)
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error adding bills", e)
                onFailure(e)
            }
        }
    }

    fun editBill(bills: Bills, updatedBills: Bills) {
        viewModelScope.launch {
            try {
                val billId: String = getBillId(bills)
                val categoryId: String = getCategoryId(bills.category)

                firestoreRepository.updateItemInFirestoreById(
                    currentUserId,
                    "Bills",
                    billId,
                    updatedBills,
                    onSuccess = {

                        dbHelper.updateBill(
                            billId = billId,
                            amount = updatedBills.amount,
                            categoryId = categoryId,
                            description = updatedBills.description,
                            selectedDueDate = updatedBills.selectedDueDate,
                            billsStatus = updatedBills.billsStatus,
                            userId = currentUserId
                        )
                        val currentBills = allBillsList.value ?: emptyList()
                        val updatedBillsList = currentBills.map {
                            if (it == bills) updatedBills else it
                        }
                        updateBills(bills = updatedBillsList)
                        Toast.makeText(
                            currentContext, "Bills edited", Toast.LENGTH_SHORT
                        ).show()
                    }
                )

            } catch (e: Exception) {
                Log.e(TAG, "Error editing Bills", e)
            }
        }
    }

    fun deleteBill(
        bill: Bills
    ) {
        viewModelScope.launch {
            try {
                val billId: String =
                    firestoreRepository.getDocumentId("Bills", currentUserId, bill)

                firestoreRepository.deleteItemFromFirestoreById(
                    currentUserId,
                    "Bills",
                    billId,
                    onSuccess = {
                        dbHelper.deleteBill(billId, currentUserId)
                        val currentBills = allBillsList.value ?: emptyList()
                        val updatedBills = currentBills.filter { it != bill }
                        updateBills(
                            bills = updatedBills
                        )
                        Toast.makeText(
                            currentContext,
                            "Bill has been deleted successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting bill", e)
            }
        }
    }

    fun generateBillId(): String {
        val random = UUID.randomUUID().toString().substring(0, 5)
        return "B$random"
    }

    private fun createBillMadeNotification(bills: Bills) {
        val notificationId = 1
        val builder = NotificationCompat.Builder(currentContext, "CHANNEL_ID")
            .setSmallIcon(R.drawable.bills_icon)
            .setContentTitle("New bill made")
            .setContentText("Bill(${bills.description}) with amount ${bills.amount} && ${bills.selectedDueDate} is due date!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(currentContext)){

            if (ActivityCompat.checkSelfPermission(
                    currentContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return println("error notifications")
            }
            notify(notificationId, builder.build())
        }
    }

    private fun createNotificationChannel(context: Context) {
        val name = "NOTIFICAITONCHANNELNAME"
        val descriptionText = "DESCRIPTION"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("CHANNEL_ID", name, importance).apply{
            description = descriptionText
        }
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun addBillToExpenses(amount: Double, category: Category, description: String, currentDate: Date,bankName: String, context: Context, bill: Bills) {
        viewModelScope.launch {
            transactionViewModel.addTransactionToFirestore(
                Transactions(
                    id = transactionViewModel.generateTransactionId(),
                    amount = amount,
                    description = description,
                    date = currentDate,
                    category = category,
                    paymentMethod = bankName,
                    transactionType = "Expenses"
                ),
                onSuccess = {
                    editBill(
                        bills = bill,
                        updatedBills = Bills(
                            id = bill.id,
                            amount = bill.amount,
                            category = bill.category,
                            description = bill.description,
                            selectedDueDate = bill.selectedDueDate,
                            billsStatus = "PAID"
                        )
                    )
                    Toast.makeText(
                        context,
                        "Bill has been added successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onFailure = {
                    Toast.makeText(
                        context,
                        "Unsuccessful to add in expense",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }
}