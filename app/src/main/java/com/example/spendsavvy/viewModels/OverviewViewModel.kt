package com.example.spendsavvy.viewModels

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendsavvy.db.DatabaseHelper
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.repo.FirestoreRepository
import kotlinx.coroutines.launch
import java.util.Calendar

class OverviewViewModel(
    context: Context,
    isOnline: Boolean,
    userId: String
) : ViewModel() {
    private val firestoreRepository = FirestoreRepository()
    private val dbHelper = DatabaseHelper(context)

    @SuppressLint("StaticFieldLeak")
    val currentContext = context
    val internet = isOnline

    val userId = userId

    val transactionsList = MutableLiveData<List<Transactions>>()
    val todayTransactionsList = MutableLiveData<List<Transactions>>()

    val expensesTotal = MutableLiveData<Double>()
    val incomesTotal = MutableLiveData<Double>()


    private fun getTransactionRecord() {
        viewModelScope.launch {

            try {
                if (internet) {
                    val transactionsFromFirestore = firestoreRepository.readItemsFromDatabase(
                        userId,
                        "Transactions",
                        Transactions::class.java
                    )

                    updateTransactions(transactionsFromFirestore)

                } else {
                    val transactionsFromSQLite = dbHelper.readTransactions(userId = userId)

                    updateTransactions(transactionsFromSQLite)
                }


            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error getting transactions", e)
            }
        }
    }

    private fun updateTransactions(transactions: List<Transactions>) {
        var totalExpenses = 0.0
        var totalIncomes = 0.0
        val todayTransaction = mutableListOf<Transactions>()
        val todayDate = Calendar.getInstance().apply {
            // Set the time part to 00:00:00
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        for (transaction in transactions) {

            if (transaction.transactionType == "Expenses") {
                totalExpenses += transaction.amount

            } else if (transaction.transactionType == "Incomes") {
                totalIncomes += transaction.amount
            }

            val transactionDate = Calendar.getInstance().apply {
                time = transaction.date
                // Set the time part to 00:00:00
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time

            if (transactionDate == todayDate) {
                todayTransaction.add(transaction)
            }

        }

        expensesTotal.postValue(totalExpenses)
        incomesTotal.postValue(totalIncomes)

        transactionsList.postValue(transactions)
        todayTransactionsList.postValue(todayTransaction)

    }

    init {
        getTransactionRecord()
    }

    private suspend fun getTransactionId(transactions: Transactions): String {
        return firestoreRepository.getDocumentId("Transactions", userId, transactions)
    }

    private suspend fun getCategoryId(category: Category): String {
        return firestoreRepository.getDocumentId("Categories", userId, category)
    }

    fun editTransaction(transactions: Transactions, updatedTransactions: Transactions) {
        viewModelScope.launch {
            try {
                val transactionId: String = getTransactionId(transactions)
                val categoryId: String = getCategoryId(transactions.category)

                firestoreRepository.updateItemInFirestoreById(
                    userId,
                    "Transactions",
                    transactionId,
                    updatedTransactions,
                    onSuccess = {

                        dbHelper.updateTransaction(
                            transactionId = transactionId,
                            amount = updatedTransactions.amount,
                            categoryId = categoryId,
                            description = updatedTransactions.description,
                            date = updatedTransactions.date,
                            transactionType = updatedTransactions.transactionType,
                            userId = userId

                        )
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
                        dbHelper.deleteTransaction(transactionId, userId)
                        getTransactionRecord()
                    }
                )


            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error deleting transaction", e)
            }
        }
    }

    fun addTransactionToFirestore(transaction: Transactions) {
        viewModelScope.launch {
            try {
                val categoryId: String = getCategoryId(transaction.category)

                firestoreRepository.addItem(
                    userId,
                    "Transactions",
                    transaction,
                    "T%04d",
                    onSuccess = { documentId ->
                        dbHelper.addNewTransaction(
                            transactionId = documentId,
                            amount = transaction.amount,
                            categoryId = categoryId,
                            description = transaction.description,
                            date = transaction.date,
                            transactionType = transaction.transactionType,
                            userId = userId
                        )
                        getTransactionRecord()
                    },
                    onFailure = { exception ->
                        Log.e(ContentValues.TAG, "Error adding transaction", exception)
                        // Handle failure
                    }
                )
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error adding transaction", e)
            }

        }
    }


}
