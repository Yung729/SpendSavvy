package com.example.spendsavvy.viewModels

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendsavvy.db.DatabaseHelper
import com.example.spendsavvy.models.Bills
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.repo.FirestoreRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.UUID

class BillsViewModel(
    context: Context,
    isOnline: Boolean,
    userId: String,
): ViewModel() {

    private val firestoreRepository = FirestoreRepository()
    private val dbHelper = DatabaseHelper(context)
    private val internet = isOnline
    private val currentContext = context
    private val currentUserId = userId
    val isLoading = MutableLiveData<Boolean>()

    val billsList = MutableLiveData<List<Bills>>()
    val allBillsList = MutableLiveData<List<Bills>>() // All bills List
    val upcomingBillsList = MutableLiveData<List<Bills>>() // upcoming bills List
    val overdueBillsList = MutableLiveData<List<Bills>>() // overdue bills List
    val paidBillsList = MutableLiveData<List<Bills>>() // paid bills List
    val totalUpcomingBills = MutableLiveData<Int>()
    val totalOverdueBills = MutableLiveData<Int>()
    val totalPaidBills = MutableLiveData<Int>()


    init {
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
                            selectedDuration = bills.selectedDuration,
                            billsStatus = bills.billsStatus,
                            userId = currentUserId
                        )
                        val currentBills = allBillsList.value ?: emptyList()
                        val updatedBills = currentBills + bills
                        updateBills(updatedBills)
                        onSuccess()
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
                            selectedDuration = updatedBills.selectedDuration,
                            billsStatus = updatedBills.billsStatus,
                            userId = currentUserId
                        )
                        val currentBills = billsList.value ?: emptyList()
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
                        val currentBills = billsList.value ?: emptyList()
                        val updatedBills = currentBills.filter { it != bill }
                        updateBills(
                            bills = updatedBills
                        )
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
}