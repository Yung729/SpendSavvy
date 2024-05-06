package com.example.spendsavvy.viewModels

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendsavvy.db.DatabaseHelper
import com.example.spendsavvy.repo.FirestoreRepository
import kotlinx.coroutines.launch

class TargetViewModel(
    context: Context,
    isOnline: Boolean,
    userId: String
) : ViewModel() {

    private val internet = isOnline
    private val firestoreRepository = FirestoreRepository()
    private val dbHelper = DatabaseHelper(context)

    val currentUserId = userId
    val budget = MutableLiveData<Double>()
    val goal = MutableLiveData<Double>()

    init {
        getBudgetAndGoal()
    }

    private fun getBudgetAndGoal(userId: String = currentUserId) {
        viewModelScope.launch {
            try {
                if (internet) {
                    val budgetFromFirestore = firestoreRepository.readSingleFieldValueFromDatabase(
                        userId, "Budget", "amount"
                    ) as Double?

                    val goalFromFirestore = firestoreRepository.readSingleFieldValueFromDatabase(
                        userId, "Goal", "amount"
                    ) as Double?

                    budget.postValue(budgetFromFirestore ?: 0.0)
                    goal.postValue(goalFromFirestore ?: 0.0)

                } else {
                    val budgetFromLocal = dbHelper.getBudget(userId)
                    val goalFromLocal = dbHelper.getGoal(userId)

                    budget.postValue(budgetFromLocal ?: 0.0)
                    goal.postValue(goalFromLocal ?: 0.0)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting budget", e)
            }
        }
    }

    fun addBudget(amount: Double) {
        firestoreRepository.addOrUpdateOneFieldItem(currentUserId, "Budget", "amount", amount,
            onSuccess = {
                // Handle success
                Log.d(TAG, "Expense added successfully ")
                dbHelper.addOrUpdateBudget(currentUserId, amount)
                getBudgetAndGoal()
            },
            onFailure = {
                // Handle failure
                Log.e(TAG, "Failed to add expense")
            }
        )
    }
    fun addGoal(amount: Double) {
        firestoreRepository.addOrUpdateOneFieldItem(currentUserId, "Goal", "amount", amount,
            onSuccess = {
                // Handle success
                Log.d(TAG, "Expense added successfully ")
                dbHelper.addOrUpdateGoal(currentUserId, amount)
                getBudgetAndGoal()
            },
            onFailure = {
                // Handle failure
                Log.e(TAG, "Failed to add expense")
            }
        )
    }

}