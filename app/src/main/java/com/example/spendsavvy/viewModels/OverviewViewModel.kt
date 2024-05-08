package com.example.spendsavvy.viewModels

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendsavvy.db.DatabaseHelper
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.repo.FirestoreRepository
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class OverviewViewModel(
    context: Context,
    isOnline: Boolean,
    userId: String,
    dateViewModel: DateSelectionViewModel
) : ViewModel() {
    private val firestoreRepository = FirestoreRepository()
    private val dbHelper = DatabaseHelper(context)

    private val internet = isOnline

    private val currentUserId = userId
    val isLoading = MutableLiveData<Boolean>()
    val selectedDateFromUser: LiveData<Date?> = dateViewModel.selectedDateFromUser

    val transactionsList = MutableLiveData<List<Transactions>>() //All transaction List
    val todayTransactionsList = MutableLiveData<List<Transactions>>() //Today transaction List
    val monthTransactionsList = MutableLiveData<List<Transactions>>() //Month transaction List
    val yearTransactionsList = MutableLiveData<List<Transactions>>() //Year transaction List

    val expensesTotal = MutableLiveData<Double>()
    val incomesTotal = MutableLiveData<Double>()
    val todayExpensesTotal = MutableLiveData<Double>()
    val todayIncomesTotal = MutableLiveData<Double>()
    val currentYearExpensesTotal = MutableLiveData<Double>()
    val currentYearIncomesTotal = MutableLiveData<Double>()
    val currentMonthExpenses = MutableLiveData<Double>()
    val currentMonthIncomes = MutableLiveData<Double>()


    //Selected Transaction List
    val selectedDateTransaction = MutableLiveData<List<Transactions>>()
    val selectedDateExpensesTotal = MutableLiveData<Double>()
    val selectedDateIncomesTotal = MutableLiveData<Double>()

    //Date
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val todayDate = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

    init {
        getTransactionRecord()
    }

    fun getTransactionRecord() {
        viewModelScope.launch {
            val transactionsFromDB: List<Transactions>

            isLoading.value = true
            try {
                transactionsFromDB = if (internet) {
                    firestoreRepository.readItemsFromDatabase(
                        currentUserId,
                        "Transactions",
                        Transactions::class.java
                    )
                } else {
                    dbHelper.readTransactions(userId = currentUserId)

                }

                updateTransactions(
                    transactionsFromDB,
                    selectedDate = selectedDateFromUser.value ?: Date()
                )


            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error getting transactions", e)
            } finally {
                isLoading.postValue(false) // Set loading state to false when loading is completed
            }
        }
    }


    private fun updateTransactions(transactions: List<Transactions>, selectedDate: Date) {

        var totalExpenses = 0.0
        var totalIncomes = 0.0
        var selectedDateExpenses = 0.0
        var selectedDateIncomes = 0.0
        var monthExpenses = 0.0
        var monthIncome = 0.0
        var todayExpenses = 0.0
        var todayIncome = 0.0
        var yearExpenses = 0.0
        var yearIncome = 0.0

        val selectedDateTransactionList = mutableListOf<Transactions>()
        val todayTransaction = mutableListOf<Transactions>()
        val monthTransaction = mutableListOf<Transactions>()
        val yearTransaction = mutableListOf<Transactions>()

        for (transaction in transactions) {

            val transactionMonth = Calendar.getInstance().apply {
                time = transaction.date
            }.get(Calendar.MONTH)

            val transactionYear = Calendar.getInstance().apply {
                time = transaction.date
            }.get(Calendar.YEAR)

            val transactionDate = Calendar.getInstance().apply {
                time = transaction.date
                // Set the time part to 00:00:00
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time

            if (transaction.transactionType == "Expenses") {
                totalExpenses += transaction.amount

            } else if (transaction.transactionType == "Incomes") {
                totalIncomes += transaction.amount
            }


            if (transactionDate == selectedDate) {
                selectedDateTransactionList.add(transaction)
                if (transaction.transactionType == "Expenses") {
                    selectedDateExpenses += transaction.amount

                } else if (transaction.transactionType == "Incomes") {
                    selectedDateIncomes += transaction.amount
                }
            }

            if (transactionDate == todayDate) {
                todayTransaction.add(transaction)
                if (transaction.transactionType == "Expenses") {
                    todayExpenses += transaction.amount

                } else if (transaction.transactionType == "Incomes") {
                    todayIncome += transaction.amount
                }
            }


            if (transactionMonth == currentMonth) {
                monthTransaction.add(transaction)
                if (transaction.transactionType == "Expenses") {
                    monthExpenses += transaction.amount
                } else if (transaction.transactionType == "Incomes") {
                    monthIncome += transaction.amount
                }
            }

            if (transactionYear == currentYear) {
                yearTransaction.add(transaction)
                if (transaction.transactionType == "Expenses") {
                    yearExpenses += transaction.amount

                } else if (transaction.transactionType == "Incomes") {
                    yearIncome += transaction.amount
                }
            }


        }

        //double
        expensesTotal.postValue(totalExpenses)
        incomesTotal.postValue(totalIncomes)
        todayExpensesTotal.postValue(todayExpenses)
        todayIncomesTotal.postValue(todayIncome)
        currentMonthExpenses.postValue(monthExpenses)
        currentMonthIncomes.postValue(monthIncome)
        currentYearExpensesTotal.postValue(yearExpenses)
        currentYearIncomesTotal.postValue(yearIncome)

        //list
        transactionsList.postValue(transactions)
        todayTransactionsList.postValue(todayTransaction)
        monthTransactionsList.postValue(monthTransaction)
        yearTransactionsList.postValue(yearTransaction)


        //selected Date
        selectedDateTransaction.postValue(selectedDateTransactionList)
        selectedDateExpensesTotal.postValue(selectedDateExpenses)
        selectedDateIncomesTotal.postValue(selectedDateIncomes)

    }


    private fun updateTransaction(
        transaction: Transactions,
        updatedTransactions: Transactions = Transactions(),
        selectedDate: Date,
        mode: Int
    ) {
        val transactionMonth = Calendar.getInstance().apply {
            time = transaction.date
        }.get(Calendar.MONTH)

        val transactionYear = Calendar.getInstance().apply {
            time = transaction.date
        }.get(Calendar.YEAR)

        val transactionDate = Calendar.getInstance().apply {
            time = transaction.date
            // Set the time part to 00:00:00
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        val amount =
            when (mode) {
                1 -> {
                    transaction.amount
                }

                2 -> {
                    updatedTransactions.amount - transaction.amount
                }

                3 -> {
                    -transaction.amount
                }

                else -> {
                    0.0
                }
            }

        if (transaction.transactionType == "Expenses") {
            expensesTotal.value = expensesTotal.value?.plus(amount)

        } else if (transaction.transactionType == "Incomes") {
            incomesTotal.value = incomesTotal.value?.plus(amount)
        }

        if (transactionDate == selectedDate) {
            selectedDateExpensesTotal.value =
                if (transaction.transactionType == "Expenses") selectedDateExpensesTotal.value?.plus(
                    amount
                ) else 0.0

            selectedDateIncomesTotal.value =
                if (transaction.transactionType == "Incomes") selectedDateIncomesTotal.value?.plus(
                    amount
                ) else 0.0
        }

        if (transactionDate == todayDate) {
            todayExpensesTotal.value =
                if (transaction.transactionType == "Expenses") todayExpensesTotal.value?.plus(
                    amount
                ) else 0.0
            todayIncomesTotal.value =
                if (transaction.transactionType == "Incomes") todayIncomesTotal.value?.plus(
                    amount
                ) else 0.0

        }

        if (transactionMonth == currentMonth) {
            currentMonthExpenses.value =
                if (transaction.transactionType == "Expenses") currentMonthExpenses.value?.plus(
                    amount
                ) else 0.0
            currentMonthIncomes.value =
                if (transaction.transactionType == "Incomes") currentMonthIncomes.value?.plus(
                    amount
                ) else 0.0
        }

        if (transactionYear == currentYear) {
            currentYearExpensesTotal.value =
                if (transaction.transactionType == "Expenses") currentYearExpensesTotal.value?.plus(
                    amount
                ) else 0.0
            currentYearIncomesTotal.value =
                if (transaction.transactionType == "Incomes") currentYearIncomesTotal.value?.plus(
                    amount
                ) else 0.0
        }


        if (mode == 1) {
            val currentTransactionsList = transactionsList.value ?: emptyList()
            val currentTodayTransactionsList = todayTransactionsList.value ?: emptyList()
            val currentMonthTransactionsList = monthTransactionsList.value ?: emptyList()
            val currentYearTransactionsList = yearTransactionsList.value ?: emptyList()
            val currentDateTransactionsList = selectedDateTransaction.value ?: emptyList()

            if (transactionDate == selectedDate) {
                selectedDateTransaction.value = currentDateTransactionsList + transaction
            }

            if (transactionDate == todayDate) {
                todayTransactionsList.value = currentTodayTransactionsList + transaction
            }


            if (transactionMonth == currentMonth) {
                monthTransactionsList.value = currentMonthTransactionsList + transaction
            }

            if (transactionYear == currentYear) {
                yearTransactionsList.value = currentYearTransactionsList + transaction
            }

            transactionsList.value = currentTransactionsList + transaction
        } else if (mode == 2) {

            selectedDateTransaction.value =
                selectedDateTransaction.value?.map { if (it == transaction) updatedTransactions else it }


            todayTransactionsList.value =
                todayTransactionsList.value?.map { if (it == transaction) updatedTransactions else it }



            monthTransactionsList.value =
                monthTransactionsList.value?.map { if (it == transaction) updatedTransactions else it }


            yearTransactionsList.value =
                yearTransactionsList.value?.map { if (it == transaction) updatedTransactions else it }


            transactionsList.value =
                transactionsList.value?.map { if (it == transaction) updatedTransactions else it }

        } else if (mode == 3) {
            selectedDateTransaction.value =
                selectedDateTransaction.value?.filter { it != transaction }


            todayTransactionsList.value =
                todayTransactionsList.value?.filter { it != transaction }



            monthTransactionsList.value =
                monthTransactionsList.value?.filter { it != transaction }

            yearTransactionsList.value =
                yearTransactionsList.value?.filter { it != transaction }


            transactionsList.value = transactionsList.value?.filter { it != transaction }
        }
    }

    private suspend fun getTransactionId(transactions: Transactions): String {
        return firestoreRepository.getDocumentId("Transactions", currentUserId, transactions)
    }

    private suspend fun getCategoryId(category: Category): String {
        return firestoreRepository.getDocumentId("Categories", currentUserId, category)
    }

    fun editTransaction(transactions: Transactions, updatedTransactions: Transactions) {
        viewModelScope.launch {
            try {
                val transactionId: String = getTransactionId(transactions)
                val categoryId: String = getCategoryId(transactions.category)

                firestoreRepository.updateItemInFirestoreById(
                    currentUserId,
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
                            userId = currentUserId

                        )

                        updateTransaction(transaction = transactions,updatedTransactions = updatedTransactions, selectedDate = selectedDateFromUser.value ?: Date(),mode = 2)
                        /*val currentTransactions = transactionsList.value ?: emptyList()
                        val updatedTransactionsList = currentTransactions.map {
                            if (it == transactions) updatedTransactions else it
                        }
                        updateTransactions(
                            transactions = updatedTransactionsList,
                            selectedDate = selectedDateFromUser.value ?: Date()
                        )*/
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
                    firestoreRepository.getDocumentId("Transactions", currentUserId, transaction)

                firestoreRepository.deleteItemFromFirestoreById(
                    currentUserId,
                    "Transactions",
                    transactionId,
                    onSuccess = {
                        dbHelper.deleteTransaction(transactionId, currentUserId)
                        updateTransaction(transaction = transaction,selectedDate = selectedDateFromUser.value ?: Date(),mode = 3)

                       /* val currentTransactions = transactionsList.value ?: emptyList()
                        val updatedTransactions = currentTransactions.filter { it != transaction }
                        updateTransactions(
                            transactions = updatedTransactions,
                            selectedDate = selectedDateFromUser.value ?: Date()
                        )*/
                    }
                )


            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error deleting transaction", e)
            }
        }
    }

    fun addTransactionToFirestore(
        transaction: Transactions,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val categoryId: String = getCategoryId(transaction.category)

                firestoreRepository.addItem(
                    currentUserId,
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
                            userId = currentUserId
                        )
                        updateTransaction(transaction = transaction,selectedDate = selectedDateFromUser.value ?: Date(),mode = 1)
                        /*val currentTransactions = transactionsList.value ?: emptyList()
                        val updatedTransactions = currentTransactions + transaction
                        updateTransactions(
                            transactions = updatedTransactions,
                            selectedDate = selectedDateFromUser.value ?: Date()
                        )*/

                        onSuccess() // Invoke the onSuccess callback
                    },
                    onFailure = { exception ->
                        Log.e(ContentValues.TAG, "Error adding transaction", exception)
                        onFailure(exception) // Invoke the onFailure callback
                    }
                )
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error adding transaction", e)
                onFailure(e) // Invoke the onFailure callback
            }
        }
    }


}
