package com.example.spendsavvy.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendsavvy.db.DatabaseHelper
import com.example.spendsavvy.models.Bills
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.Question
import com.example.spendsavvy.models.Staff
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.repo.FirestoreRepository
import kotlinx.coroutines.launch

class MainViewModel(
    context: Context, isOnline: Boolean, userId: String
) : ViewModel() {

    private val databaseHelper = DatabaseHelper(context)
    private val currentUserId = userId
    private val firestoreRepo = FirestoreRepository()

    fun syncDatabase() {
        if (databaseHelper.isDatabaseEmpty()) {
            viewModelScope.launch {
                try {
                    migrateFirestoreDataToSQLite(currentUserId)
                } catch (e: Exception) {
                    Log.e("Migrate", "Error migrate", e)
                }

                checkTotalSalary(currentUserId)
            }
        }


    }

    private suspend fun checkTotalSalary(userId: String) {
        val totalSalaryTransactionFirestore = firestoreRepo.readItemsFromDatabase(
            userId,
            "Transactions",
            Transactions::class.java
        ).filter {
            it.category.categoryName == "Total Staff Salary"
        }

        val totalSalaryTransactionsLocal = databaseHelper.getTransactionByCategoryName(
            userId,
            "Total Staff Salary"
        )

        for (transactionFirestore in totalSalaryTransactionFirestore) {
            // Check if the transaction already exists locally
            val existsLocally = totalSalaryTransactionsLocal.any {
                it.id == transactionFirestore.id
            }

            if (!existsLocally) {

                val documentId =
                    firestoreRepo.getDocumentId("Transactions", userId, transactionFirestore)
                val categoryId =
                    firestoreRepo.getDocumentId("Category", userId, transactionFirestore.category)

                // If the transaction doesn't exist locally, add it to the database
                databaseHelper.addNewTransaction(
                    transactionId = documentId, // Assuming you have documentId defined somewhere
                    internalId = transactionFirestore.id,
                    amount = transactionFirestore.amount,
                    categoryId = categoryId, // Assuming you have categoryId defined somewhere
                    paymentMethod = "Cash",
                    description = transactionFirestore.description,
                    date = transactionFirestore.date,
                    transactionType = transactionFirestore.transactionType,
                    userId = userId
                )
            }
        }
    }

    private suspend fun migrateFirestoreDataToSQLite(userId: String) {


        try {
            // Read data from Firestore collections
            val categories =
                firestoreRepo.readItemsFromDatabase(userId, "Categories", Category::class.java)
            val transactions = firestoreRepo.readItemsFromDatabase(
                userId,
                "Transactions",
                Transactions::class.java
            )
            val staff = firestoreRepo.readItemsFromDatabase(
                userId,
                "Staff",
                Staff::class.java
            )
            val bill = firestoreRepo.readItemsFromDatabase(
                userId,
                "Bills",
                Bills::class.java
            )
            val question = firestoreRepo.readItemsFromDatabase(
                userId,
                "Questions",
                Question::class.java
            )
            val budget =
                firestoreRepo.readSingleFieldValueFromDatabase(userId, "Budget", "amount")
            val goal =
                firestoreRepo.readSingleFieldValueFromDatabase(userId, "Goal", "amount")


            databaseHelper.addNewCategoryList(categories, userId)
            databaseHelper.addNewTransactionsList(transactions, userId)
            databaseHelper.addNewStaffList(staff, userId)
            databaseHelper.addNewBillsList(bill, userId)
            databaseHelper.addNewQuestionsList(question, userId)

            if (budget != null) {
                databaseHelper.addOrUpdateBudget(userId, budget as Double)
            }
            if (goal != null) {
                databaseHelper.addOrUpdateGoal(userId, goal as Double)
            }



            Log.e("Success", "Success migrating Firestore data to SQLite")
        } catch (e: Exception) {
            Log.e("TAG", "Error migrating Firestore data to SQLite: $e")
        }

    }
}