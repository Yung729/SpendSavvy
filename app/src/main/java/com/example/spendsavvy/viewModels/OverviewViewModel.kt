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
    val transactionsList = MutableLiveData<List<Transactions>>()
    val expensesTotal = MutableLiveData<Double>()
    val incomesTotal = MutableLiveData<Double>()


    private fun getTransactionRecord() {
        viewModelScope.launch {
            try {
                val transactions = firestoreRepository.readItemsFromDatabase(
                    userId,
                    "Transactions",
                    Transactions::class.java
                )

                var totalExpenses = 0.0
                var totalIncomes = 0.0


                for (transaction in transactions) {
                    if (transaction.transactionType == "Expenses") {
                        totalExpenses += transaction.amount

                    } else if (transaction.transactionType == "Incomes") {
                        totalIncomes += transaction.amount
                    }
                }

                expensesTotal.postValue(totalExpenses)
                incomesTotal.postValue(totalIncomes)

                transactionsList.postValue(transactions)


            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error getting transactions", e)
            }
        }
    }

    init {
        getTransactionRecord()
    }

    private suspend fun getTransactionId(transactions: Transactions): String {
        return firestoreRepository.getDocumentId("Transactions", userId, transactions)
    }

    fun editTransaction(transactions: Transactions, updatedTransactions: Transactions) {
        viewModelScope.launch {
            try {
                val categoryId: String = getTransactionId(transactions)

                firestoreRepository.updateItemInFirestoreById(
                    userId,
                    "Transactions",
                    categoryId,
                    updatedTransactions,
                    onSuccess = {
                        getTransactionRecord()
                    }
                )

            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error editing Transactions", e)
            }
        }
    }

    fun deleteTransaction(transaction: Transactions) {
        viewModelScope.launch {
            try {
                val transactionId: String =
                    firestoreRepository.getDocumentId("Transactions", userId, transaction)

                firestoreRepository.deleteItemFromFirestoreById(
                    userId,
                    "Transactions",
                    transactionId,
                    onSuccess = {
                        getTransactionRecord()
                    }
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
