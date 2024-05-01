package com.example.spendsavvy.viewModels

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendsavvy.db.DatabaseHelper
import com.example.spendsavvy.repo.FirestoreRepository
import kotlinx.coroutines.launch

class BudgetViewModel(
    context: Context,
    isOnline: Boolean,
    userId: String
) : ViewModel() {

    @SuppressLint("StaticFieldLeak")
    val currentContext = context
    private val internet = isOnline
    private val firestoreRepository = FirestoreRepository()
    private val dbHelper = DatabaseHelper(context)

    val currentUserId = userId
    val budget = MutableLiveData<Double>()

    init {
        getBudget()
    }

    private fun getBudget(userId: String = currentUserId) {
        viewModelScope.launch {
            try {
                if (internet) {
                    val budgetFromFirestore = firestoreRepository.readSingleFieldValueFromDatabase(
                        userId, "Budget", "amount"
                    ) as Double?

                    budget.postValue(budgetFromFirestore ?: 0.0)

                } else {
                    val budgetFromLocal = dbHelper.getBudget(userId)

                    budget.postValue(budgetFromLocal ?: 0.0)
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
                getBudget()
            },
            onFailure = {
                // Handle failure
                Log.e(TAG, "Failed to add expense")
            }
        )
    }

}