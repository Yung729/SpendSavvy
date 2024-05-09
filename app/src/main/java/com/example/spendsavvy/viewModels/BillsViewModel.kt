package com.example.spendsavvy.viewModels

import android.content.ContentValues
import android.content.Context
import android.util.Log
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

    private val currentUserId = userId
    val isLoading = MutableLiveData<Boolean>()

    val billsList = MutableLiveData<List<Bills>>() // All bills List
    val upcomingBillsList = MutableLiveData<List<Bills>>() // upcoming bills List
    val overdueBillsList = MutableLiveData<List<Bills>>() // overdue bills List
    val paidBillsList = MutableLiveData<List<Bills>>() // paid bills List

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

                updateBills(
                    billsFromDB,
                )

            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error getting bills", e)
            } finally {
                isLoading.postValue(false) // Set loading state to false when loading is completed
            }
        }
    }

    private fun updateBills(bills: List<Bills>) {
        //change the bill data
    }

    private suspend fun getBillId(bills: Bills): String {
        return firestoreRepository.getDocumentId("Bills", currentUserId, bills)
    }

    private suspend fun getCategoryId(category: Category): String {
        return firestoreRepository.getDocumentId("Categories", currentUserId, category)
    }
//
//    fun editBill(bills: Bills, updatedBills: Bills) {
//        viewModelScope.launch {
//            try {
//                val billId: String = getBillId(bills)
//                val categoryId: String = getCategoryId(bills.category)
//
//                firestoreRepository.updateItemInFirestoreById(
//                    currentUserId,
//                    "Bills",
//                    billId,
//                    updatedBills,
//                    onSuccess = {
//
//                        dbHelper.updateBill(
//                            billId = billId,
//                            amount = updatedBills.amount,
//                            categoryId = categoryId,
//                            description = updatedBills.description,
//                            selectedDueDate = updatedBills.selectedDueDate,
//                            selectedDuration = updatedBills.selectedDuration,
//                            billsStatus = updatedBills.billsStatus,
//                            userId = currentUserId
//
//                        )
//                        val currentBills = billsList.value ?: emptyList()
//                        val updatedBillsList = currentBills.map {
//                            if (it == bills) updatedBills else it
//                        }
//                        updateBills(
//                            bills = updatedBillsList,
//                        )
//                    }
//                )
//
//            } catch (e: Exception) {
//                Log.e(ContentValues.TAG, "Error editing Bills", e)
//            }
//        }
//    }
//
//    fun deleteBill(bill: Bills) {
//        viewModelScope.launch {
//            try {
//                val billId: String =
//                    firestoreRepository.getDocumentId("Bills", currentUserId, bill)
//
//                firestoreRepository.deleteItemFromFirestoreById(
//                    currentUserId,
//                    "Bills",
//                    billId,
//                    onSuccess = {
//                        dbHelper.deleteBill(billId, currentUserId)
//                        val currentBills = billsList.value ?: emptyList()
//                        val updatedBills = currentBills.filter { it != bill }
//                        updateBills(
//                            bills = updatedBills,
//                        )
//                    }
//                )
//
//
//            } catch (e: Exception) {
//                Log.e(ContentValues.TAG, "Error deleting bill", e)
//            }
//        }
//    }
//
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
                        onSuccess() // Invoke the onSuccess callback
                    },
                    onFailure = { exception ->
                        Log.e(ContentValues.TAG, "Error adding bills", exception)
                        onFailure(exception)
                    }
                )
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error adding bills", e)
                onFailure(e)
            }
        }
    }

    fun generateBillId(): String {
        val random = UUID.randomUUID().toString().substring(0, 5)
        return "B$random"
    }
}