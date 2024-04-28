package com.example.spendsavvy.viewModels

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendsavvy.db.DatabaseHelper
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.repo.FirestoreRepository
import kotlinx.coroutines.launch

class OverviewViewModel(
    context: Context
) : ViewModel() {
    private val firestoreRepository = FirestoreRepository()
    private val dbHelper = DatabaseHelper(context)

    @SuppressLint("StaticFieldLeak")
    val currentContext = context

    val userId = "JqPinxCQzIV5Tcs9dKxul6h49192"
    val transactionsList = MutableLiveData<List<Transactions>>()
    val expensesTotal = MutableLiveData<Double>()
    val incomesTotal = MutableLiveData<Double>()


    private fun getTransactionRecord(context: Context = currentContext) {
        viewModelScope.launch {

            try {
                if (isInternetAvailable(context)) {
                    val transactionsFromFirestore = firestoreRepository.readItemsFromDatabase(
                        userId,
                        "Transactions",
                        Transactions::class.java
                    )

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

                    updateCTransactions(transactionsFromFirestore)

                } else {
                    val transactionsFromSQLite = dbHelper.readTransactions()

                    updateCTransactions(transactionsFromSQLite)
                }


            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error getting transactions", e)
            }
        }
    }

    private fun updateCTransactions(transactions: List<Transactions>) {
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

    private fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }
        return result
    }

}
