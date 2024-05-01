package com.example.spendsavvy.viewModels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendsavvy.db.DatabaseHelper
import com.example.spendsavvy.models.BankAccount
import com.example.spendsavvy.models.Cash
import com.example.spendsavvy.models.FDAccount
import com.example.spendsavvy.models.Stock
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.repo.FirestoreRepository
import kotlinx.coroutines.launch
import java.util.Calendar

class WalletViewModel(
    context: Context,
    userId: String
) : ViewModel() {

    private val firestoreRepository = FirestoreRepository()
    private val dbHelper = DatabaseHelper(context)

    /*    @SuppressLint("StaticFieldLeak")
    val currentContext = context
    private val internet = isOnline*/

    val cashDetailsList = MutableLiveData<List<Cash>>()
    val bankAccountList = MutableLiveData<List<BankAccount>>()
    val FDAccountList = MutableLiveData<List<FDAccount>>()
    val stockList = MutableLiveData<List<Stock>>()              //hold on

    val userId = userId

    private fun getCashDetails() {
        viewModelScope.launch {
            val cashDetailsFromFirestore = firestoreRepository.readItemsFromDatabase(
                userId,
                "Cash",
                Cash::class.java
            )

            updateCashDetails(cashDetailsFromFirestore)

        }
    }

    private fun updateCashDetails(cash: List<Cash>){
        var totalBalance = 0.0

        for (details in cash) {
            totalBalance += details.balance
        }

    }

    private fun getBankAccountDetails() {
        viewModelScope.launch {
            val bankDetailsFromFirestore = firestoreRepository.readItemsFromDatabase(
                userId,
                "BankAccount",
                BankAccount::class.java
            )

            updateBankAccountDetails(bankDetailsFromFirestore)

        }
    }

    private fun updateBankAccountDetails(bankAccount: List<BankAccount>){
        var totalBalance = 0.0

        for (details in bankAccount) {
            totalBalance += details.balance
        }

    }

    private fun getFDAccountDetails() {
        viewModelScope.launch {
            val FdDetailsFromFirestore = firestoreRepository.readItemsFromDatabase(
                userId,
                "FDAccount",
                FDAccount::class.java
            )

            updateFDAccountDetails(FdDetailsFromFirestore)

        }
    }

    private fun updateFDAccountDetails(bankAccount: List<FDAccount>){
        var totalDeposit = 0.0

        for (details in bankAccount) {
            totalDeposit += details.deposit
        }

    }



}