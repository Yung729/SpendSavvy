package com.example.spendsavvy.viewModels

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendsavvy.db.DatabaseHelper
import com.example.spendsavvy.models.Cash
import com.example.spendsavvy.models.Category
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
    val FDAccountList = MutableLiveData<List<FDAccount>>()
    val stockList = MutableLiveData<List<Stock>>()              //hold on

    val balanceTotal = MutableLiveData<Double>()
    val userId = userId

    private fun getCashDetails(
    ) {
        viewModelScope.launch {
            try{

                val cashDetailsFromFirestore = firestoreRepository.readWalletItemsFromDatabase(
                    userId,
                    "Cash",
                    Cash::class.java
                )

                updateCashBalance(cashDetailsFromFirestore)

            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error getting cash details", e)
            }
        }
    }

    private fun updateCashBalance(cash: List<Cash>){
        var totalBalance = 0.0

        for (cashAccount in cash){
            totalBalance += cashAccount.balance
        }

        balanceTotal.postValue(totalBalance)
        cashDetailsList.postValue(cash)
    }

    private suspend fun getTypeName(cash: Cash): String {
        return firestoreRepository.getDocumentId("Cash", userId, cash)
    }
    fun editCashDetails(cash: Cash, updatedCashDetails: Cash){
        viewModelScope.launch{
            try{
                val typeName = getTypeName(cash)

                firestoreRepository.updateWalletItemsInFirestoreByName(
                    userId,
                    "Cash",
                    typeName,
                    updatedCashDetails,
                    onSuccess = {
                        dbHelper.updateCashDetails(
                            updatedCashDetails.type,
                            typeName,
                            updatedCashDetails.balance,
                            userId
                        )

                        getCashDetails()
                    }
                )
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error editing cash details", e)
            }
        }
    }

    fun addCashDetailsToDatabase(cash: Cash){
        viewModelScope.launch {
            try {
                firestoreRepository.addWalletItems(
                    userId,
                    "Cash",
                    cash,
                    "%s",
                    onSuccess = {
                        dbHelper.addNewCashDetails(
                            type = cash.type,
                            typeName = cash.typeName,
                            balance = cash.balance,
                            userId = userId
                        )
                        getCashDetails()
                    },
                    onFailure = { exception ->
                        Log.e(ContentValues.TAG, "Error adding cash details", exception)
                    }
                )
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error adding cash details", e)
            }
        }
    }

    private fun getFDAccountDetails() {
        viewModelScope.launch {
            val FdDetailsFromFirestore = firestoreRepository.readItemsFromDatabase(
                userId,
                "FDAccount",
                FDAccount::class.java
            )


        }
    }

    /*private fun updateFDAccountDetails(bankAccount: List<FDAccount>){
        var totalDeposit = 0.0

        for (details in bankAccount) {
            totalDeposit += details.deposit
        }

    }*/



}