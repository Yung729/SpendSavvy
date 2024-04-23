package com.example.spendsavvy.viewModels

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.repo.FirestoreRepository
import kotlinx.coroutines.launch

class OverviewViewModel : ViewModel() {
    private val firestoreRepository = FirestoreRepository()


    val userId = "JqPinxCQzIV5Tcs9dKxul6h49192"
    val transactionsList = MutableLiveData<List<Transactions>>(emptyList())

    private fun getTransactionRecord() {
        viewModelScope.launch {
            try {
                val transactions = firestoreRepository.readItemsFromDatabase(
                    userId,
                    "Transactions",
                    Transactions::class.java
                )

                transactionsList.postValue(transactions)

            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error getting transactions", e)
            }
        }
    }

    init {
        getTransactionRecord()
    }

    fun deleteTransaction(transaction: Transactions) {
        viewModelScope.launch {
            try {
                val transactionId: String =
                    firestoreRepository.getDocumentId("Transactions", userId, transaction)

                firestoreRepository.deleteItemFromFirestoreById(
                    userId,
                    "Transactions",
                    transactionId
                )

            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error deleting transaction", e)
            }
        }
    }

    fun addTransactionToFirestore(transaction: Transactions) {

        // Get the latest transaction ID to generate a new one
        firestoreRepository.addItem(
            userId,
            "Transactions",
            transaction,
            "T%04d",
            onSuccess = {
                getTransactionRecord()
            },
            onFailure = { exception ->
                Log.e(ContentValues.TAG, "Error adding category", exception)
                // Handle failure
            }
        )
    }

}
