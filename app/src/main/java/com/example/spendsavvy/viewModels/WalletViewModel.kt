package com.example.spendsavvy.viewModels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendsavvy.db.DatabaseHelper
import com.example.spendsavvy.models.BankAccount
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.repo.FirestoreRepository
import kotlinx.coroutines.launch

class WalletViewModel(
    context: Context,
    userId: String
) : ViewModel() {

    private val firestoreRepository = FirestoreRepository()
    private val dbHelper = DatabaseHelper(context)

    /*    @SuppressLint("StaticFieldLeak")
    val currentContext = context
    private val internet = isOnline*/

    val bankAccountList = MutableLiveData<List<BankAccount>>()
    val userId = userId

    private fun getCashDetails() {
        viewModelScope.launch {
            val transactionsFromFirestore = firestoreRepository.readItemsFromDatabase(
                userId,
                "Cash",
                Transactions::class.java
            )

            //sync to SQLite
            dbHelper.deleteAllTransaction()
            dbHelper.resetPrimaryKey("cash")
            transactionsFromFirestore.forEach { cash ->
                dbHelper.addNewCashDetails(

                )
            }
        }
    }
}