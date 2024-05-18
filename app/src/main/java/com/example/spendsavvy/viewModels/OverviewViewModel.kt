package com.example.spendsavvy.viewModels

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
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
import java.util.UUID

class OverviewViewModel(
    context: Context,
    isOnline: Boolean,
    userId: String,
    dateViewModel: DateSelectionViewModel,
    val walletViewModel: WalletViewModel
) : ViewModel() {

    private val firestoreRepository = FirestoreRepository()
    private val dbHelper = DatabaseHelper(context)
    private val currentContext = context
    private val internet = isOnline

    private val currentUserId = userId
    val isLoading = MutableLiveData<Boolean>()
    private val selectedDateFromUser: LiveData<Date?> = dateViewModel.selectedDateFromUser

    private val selectedStartDate: LiveData<Date?> = dateViewModel.selectedStartDate
    private val selectedEndDate: LiveData<Date?> = dateViewModel.selectedEndDate

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
    val dateRangeExpensesTotal = MutableLiveData<Double>()
    val dateRangeIncomesTotal = MutableLiveData<Double>()


    //Selected Transaction List
    val selectedDateTransaction = MutableLiveData<List<Transactions>>()
    val selectedDateExpensesTotal = MutableLiveData<Double>()
    val selectedDateIncomesTotal = MutableLiveData<Double>()

    val selectedDateRangeTransaction = MutableLiveData<List<Transactions>>()

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
        if (selectedDateFromUser.value == null) {
            dateViewModel.setSelectedDate(Calendar.getInstance().apply {
                time = Date()
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time)
        }
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
                    transactionsFromDB
                )


            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error getting transactions", e)
            } finally {
                isLoading.postValue(false)
            }
        }
    }


    //update with the list no changed
    fun updateTransactions(
        transactions: List<Transactions> = transactionsList.value as List<Transactions>,
        selectedDate: Date = selectedDateFromUser.value ?: Date(),
        startDate: Date = selectedStartDate.value ?: Date(),
        endDate: Date = selectedEndDate.value ?: Date()
    ) {
        viewModelScope.launch {
            val selectedDateTransactionList = transactions.filter {
                Calendar.getInstance().apply {
                    time = it.date
                    // Set the time part to 00:00:00
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.time == selectedDate
            }

            val todayTransaction = transactions.filter {
                Calendar.getInstance().apply {
                    time = it.date
                    // Set the time part to 00:00:00
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.time == todayDate
            }
            val monthTransaction = transactions.filter {
                Calendar.getInstance().apply { time = it.date }
                    .get(Calendar.MONTH) == currentMonth &&
                        Calendar.getInstance().apply { time = it.date }
                            .get(Calendar.YEAR) == currentYear
            }
            val yearTransaction = transactions.filter {
                Calendar.getInstance().apply { time = it.date }.get(Calendar.YEAR) == currentYear
            }
            val dateRangeTransactions = transactions.filter { it.date in startDate..endDate }

            val totalExpenses =
                transactions.filter { it.transactionType == "Expenses" }.sumOf { it.amount }
            val totalIncomes =
                transactions.filter { it.transactionType == "Incomes" }.sumOf { it.amount }
            val todayExpenses =
                todayTransaction.filter { it.transactionType == "Expenses" }.sumOf { it.amount }
            val todayIncome =
                todayTransaction.filter { it.transactionType == "Incomes" }.sumOf { it.amount }
            val monthExpenses =
                monthTransaction.filter { it.transactionType == "Expenses" }.sumOf { it.amount }
            val monthIncome =
                monthTransaction.filter { it.transactionType == "Incomes" }.sumOf { it.amount }
            val yearExpenses =
                yearTransaction.filter { it.transactionType == "Expenses" }.sumOf { it.amount }
            val yearIncome =
                yearTransaction.filter { it.transactionType == "Incomes" }.sumOf { it.amount }
            val dateRangeExpenses =
                dateRangeTransactions.filter { it.transactionType == "Expenses" }
                    .sumOf { it.amount }
            val dateRangeIncomes =
                dateRangeTransactions.filter { it.transactionType == "Incomes" }.sumOf { it.amount }

            expensesTotal.postValue(totalExpenses)
            incomesTotal.postValue(totalIncomes)
            todayExpensesTotal.postValue(todayExpenses)
            todayIncomesTotal.postValue(todayIncome)
            currentMonthExpenses.postValue(monthExpenses)
            currentMonthIncomes.postValue(monthIncome)
            currentYearExpensesTotal.postValue(yearExpenses)
            currentYearIncomesTotal.postValue(yearIncome)
            dateRangeExpensesTotal.postValue(dateRangeExpenses)
            dateRangeIncomesTotal.postValue(dateRangeIncomes)

            transactionsList.postValue(transactions)
            todayTransactionsList.postValue(todayTransaction)
            monthTransactionsList.postValue(monthTransaction)
            yearTransactionsList.postValue(yearTransaction)
            selectedDateRangeTransaction.postValue(dateRangeTransactions)

            selectedDateTransaction.postValue(selectedDateTransactionList)
            selectedDateExpensesTotal.postValue(selectedDateTransactionList.filter { it.transactionType == "Expenses" }
                .sumOf { it.amount })
            selectedDateIncomesTotal.postValue(selectedDateTransactionList.filter { it.transactionType == "Incomes" }
                .sumOf { it.amount })
        }
    }


    private suspend fun getTransactionId(transactions: Transactions): String {
        return firestoreRepository.getDocumentId("Transactions", currentUserId, transactions)
    }

    suspend fun getCategoryId(category: Category): String {
        return firestoreRepository.getDocumentId("Categories", currentUserId, category)
    }

    fun editTransaction(transactions: Transactions, updatedTransactions: Transactions) {
        viewModelScope.launch {
            try {
                val transactionId: String = getTransactionId(transactions)
                val categoryId: String = getCategoryId(transactions.category)

                // Calculate the difference in amount
                val amountDifference = updatedTransactions.amount - transactions.amount

                // Calculate the balance adjustment based on transaction type
                val balanceAdjustment = when (updatedTransactions.transactionType) {
                    "Incomes" -> amountDifference
                    "Expenses" -> -amountDifference
                    else -> 0.0
                }

                walletViewModel.updateCashBalance(
                    updatedTransactions.paymentMethod,
                    balanceAdjustment
                ) {
                    firestoreRepository.updateItemInFirestoreById(
                        currentUserId,
                        "Transactions",
                        transactionId,
                        updatedTransactions,
                        onSuccess = {

                            dbHelper.updateTransaction(
                                transactionId = transactionId,
                                internalId = updatedTransactions.id,
                                amount = updatedTransactions.amount,
                                categoryId = categoryId,
                                paymentMethod = updatedTransactions.paymentMethod,
                                description = updatedTransactions.description,
                                date = updatedTransactions.date,
                                transactionType = updatedTransactions.transactionType,
                                userId = currentUserId

                            )

                            val currentTransactions = transactionsList.value ?: emptyList()
                            val updatedTransactionsList = currentTransactions.map {
                                if (it == transactions) updatedTransactions else it
                            }
                            updateTransactions(
                                transactions = updatedTransactionsList
                            )
                            Toast.makeText(
                                currentContext, "Transaction edited", Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }

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

                walletViewModel.updateCashBalance(
                    transaction.paymentMethod,
                    when (transaction.transactionType) {
                        "Incomes" -> {
                            -transaction.amount
                        }

                        "Expenses" -> {
                            transaction.amount
                        }

                        else -> 0.0
                    }
                ) {
                    firestoreRepository.deleteItemFromFirestoreById(
                        currentUserId,
                        "Transactions",
                        transactionId,
                        onSuccess = {
                            dbHelper.deleteTransaction(transactionId, currentUserId)

                            val currentTransactions = transactionsList.value ?: emptyList()
                            val updatedTransactions =
                                currentTransactions.filter { it != transaction }
                            updateTransactions(
                                transactions = updatedTransactions,
                                selectedDate = selectedDateFromUser.value ?: Date()
                            )
                            Toast.makeText(
                                currentContext, "Transaction deleted", Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }


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

                walletViewModel.updateCashBalance(
                    transaction.paymentMethod,
                    when (transaction.transactionType) {
                        "Incomes" -> {
                            transaction.amount
                        }

                        "Expenses" -> {
                            -transaction.amount
                        }

                        else -> 0.0
                    }
                ) {


                    firestoreRepository.addItem(
                        currentUserId,
                        "Transactions",
                        transaction,
                        "T%04d",
                        onSuccess = { documentId ->
                            dbHelper.addNewTransaction(
                                transactionId = documentId,
                                internalId = transaction.id,
                                amount = transaction.amount,
                                categoryId = categoryId,
                                paymentMethod = transaction.paymentMethod,
                                description = transaction.description,
                                date = transaction.date,
                                transactionType = transaction.transactionType,
                                userId = currentUserId
                            )


                            val currentTransactions = transactionsList.value ?: emptyList()
                            val updatedTransactions = currentTransactions + transaction
                            updateTransactions(
                                transactions = updatedTransactions
                            )

                            onSuccess() // Invoke the onSuccess callback
                        },
                        onFailure = { exception ->
                            Log.e(ContentValues.TAG, "Error adding transaction", exception)
                            onFailure(exception) // Invoke the onFailure callback
                        }
                    )
                }


            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error adding transaction", e)
                onFailure(e) // Invoke the onFailure callback
            }
        }
    }


    fun addTransactionToAllUser(
        userId: String,
        transaction: Transactions,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val categoryId: String = getCategoryId(transaction.category)

                firestoreRepository.updateWalletBalanceInFirestore(userId = userId,
                    typeName = transaction.paymentMethod,
                    when (transaction.transactionType) {
                        "Incomes" -> {
                            transaction.amount
                        }

                        "Expenses" -> {
                            -transaction.amount
                        }

                        else -> 0.0
                    },
                    onSuccess = {
                        firestoreRepository.addItem(
                            userId,
                            "Transactions",
                            transaction,
                            "T%04d",
                            onSuccess = { documentId ->
                                dbHelper.addNewTransaction(
                                    transactionId = documentId,
                                    internalId = transaction.id,
                                    amount = transaction.amount,
                                    categoryId = categoryId,
                                    paymentMethod = transaction.paymentMethod,
                                    description = transaction.description,
                                    date = transaction.date,
                                    transactionType = transaction.transactionType,
                                    userId = userId
                                )
                                val currentTransactions = transactionsList.value ?: emptyList()
                                val updatedTransactions = currentTransactions + transaction
                                updateTransactions(
                                    transactions = updatedTransactions
                                )

                                onSuccess() // Invoke the onSuccess callback
                            },
                            onFailure = { exception ->
                                Log.e(ContentValues.TAG, "Error adding transaction", exception)
                                onFailure(exception) // Invoke the onFailure callback
                            }
                        )
                    },
                    onFailure = {})


            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error adding transaction", e)
                onFailure(e) // Invoke the onFailure callback
            }
        }
    }

    fun generateTransactionId(): String {
        val random = UUID.randomUUID().toString().substring(0, 9)
        return "T$random"
    }


}
