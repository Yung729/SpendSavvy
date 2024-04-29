package com.example.spendsavvy.viewModels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spendsavvy.db.DatabaseHelper
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.repo.FirestoreRepository

class WalletViewModel(
    context: Context,
    isOnline: Boolean
) : ViewModel() {

    private val firestoreRepository = FirestoreRepository()
    private val dbHelper = DatabaseHelper(context)


    @SuppressLint("StaticFieldLeak")
    val currentContext = context
    private val internet = isOnline

    val userId = "JqPinxCQzIV5Tcs9dKxul6h49192"
}