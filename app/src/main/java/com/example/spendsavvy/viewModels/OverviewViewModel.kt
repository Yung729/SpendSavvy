package com.example.spendsavvy.viewModels

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendsavvy.db.DatabaseHelper
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.repo.FirestoreRepository
import kotlinx.coroutines.launch
import java.util.Calendar

class OverviewViewModel(
    context: Context,
    isOnline: Boolean
) : ViewModel() {
    private val firestoreRepository = FirestoreRepository()
    private val dbHelper = DatabaseHelper(context)

    @SuppressLint("StaticFieldLeak")
    val currentContext = context
    val internet = isOnline

    val userId = "JqPinxCQzIV5Tcs9dKxul6h49192"

    val transactionsList = MutableLiveData<List<Transactions>>()
    val todayTransactionsList = MutableLiveData<List<Transactions>>()

    val expensesTotal = MutableLiveData<Double>()
    val incomesTotal = MutableLiveData<Double>()


    private fun getTransactionRecord(context: Context = currentContext) {
        viewModelScope.launch {

            try {
                if (internet) {
                    val transactionsFromFirestore = firestoreRepository.readItemsFromDatabase(
                        userId,
                        "Transactions",
                        Transactions::class.java
                    )

                    //sync to SQLite
                    dbHelper.deleteAllTransaction()
                    dbHelper.resetPrimaryKey("transactions")
                    transactionsFromFirestore.forEach { transaction ->
                        dbHelper.addNewTransaction(
                            amount = transaction.amount,
                            categoryId = dbHelper.getCategoryId(transaction.category.categoryName),
                            description = transaction.description,
                            date = transaction.date,
                            transactionType = transaction.transactionType
                        )
                    }

                    updateTransactions(transactionsFromFirestore)

                } else {
                    val transactionsFromSQLite = dbHelper.readTransactions()

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
