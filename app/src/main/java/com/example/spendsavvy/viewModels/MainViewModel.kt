package com.example.spendsavvy.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendsavvy.db.DatabaseHelper
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.Staff
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.repo.FirestoreRepository
import kotlinx.coroutines.launch

class MainViewModel(
    context: Context, isOnline: Boolean, userId: String
) : ViewModel() {

    private val databaseHelper = DatabaseHelper(context)
    private val currentUserId = userId

    fun syncDatabase() {
        if (databaseHelper.isDatabaseEmpty()) {
            viewModelScope.launch {
                try {
                    migrateFirestoreDataToSQLite(currentUserId)
                } catch (e: Exception) {
                    Log.e("Migrate", "Error migrate", e)
                }

            }
        }
    }


    private suspend fun migrateFirestoreDataToSQLite(userId: String) {

        val firestoreRepo = FirestoreRepository()


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
            val budget =
                firestoreRepo.readSingleFieldValueFromDatabase(userId, "Budget", "amount")
            val goal =
                firestoreRepo.readSingleFieldValueFromDatabase(userId, "Goal", "amount")


            databaseHelper.addNewCategoryList(categories, userId)
            databaseHelper.addNewTransactionsList(transactions, userId)
            databaseHelper.addNewStaffList(staff, userId)

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