package com.example.spendsavvy.viewModels

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendsavvy.db.DatabaseHelper
import com.example.spendsavvy.models.Cash
import com.example.spendsavvy.models.FDAccount
import com.example.spendsavvy.models.Stock
import com.example.spendsavvy.repo.FirestoreRepository
import kotlinx.coroutines.launch
import java.util.UUID

class WalletViewModel(
    context: Context,
    userId: String
) : ViewModel() {

    private val firestoreRepository = FirestoreRepository()
    private val dbHelper = DatabaseHelper(context)

    val cashDetailsList = MutableLiveData<List<Cash>>()
    val fdAccDetailsList = MutableLiveData<List<FDAccount>>()
    val stockListLive = MutableLiveData<List<Stock>>()

    //stock
    val totalPriceStock = MutableLiveData<Double>()

    val userId = userId
    val currentContext = context


    init {
        getCashDetails()
        getFDAccountDetails()
        getStockDetails()
    }

    //CASH DETAILS
    private fun getCashDetails(
    ) {
        viewModelScope.launch {
            try {

                val cashDetailsFromFirestore = firestoreRepository.readWalletItemsFromDatabase(
                    userId,
                    "Cash",
                    Cash::class.java
                )

                updateCashInfo(cashDetailsFromFirestore)

            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error getting cash details", e)
            }
        }
    }

    private fun updateCashInfo(cash: List<Cash>) {
        cashDetailsList.postValue(cash)
    }

    private suspend fun getTypeName(cash: Cash): String {
        return firestoreRepository.getDocumentId("Cash", userId, cash)
    }

    fun editCashDetails(cash: Cash, updatedCashDetails: Cash) {
        /*if (cashDetailsList.value?.any { it.typeName == cash.typeName } == false) {
            Toast.makeText(
                currentContext, "The cash account has not added yet, create one", Toast.LENGTH_SHORT
            ).show()
            return
        }*/

        viewModelScope.launch {
            try {
                val typeName = getTypeName(cash)

                firestoreRepository.updateWalletItemsInFirestoreByName(
                    userId,
                    "Cash",
                    typeName,
                    updatedCashDetails,
                    onSuccess = {
                        /*dbHelper.updateCashDetails(
                            updatedCashDetails.type,
                            typeName,
                            updatedCashDetails.balance,
                            userId
                        )*/

                        val cashInfo = cashDetailsList.value ?: emptyList()
                        val updatedCashDetailsList = cashInfo.map {
                            if (it == cash) updatedCashDetails else it
                        }

                        updateCashInfo(
                            cash = updatedCashDetailsList
                        )
                    }
                )
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error editing cash details", e)
            }
        }
    }

    fun addCashDetailsToDatabase(cash: Cash) {

        if (cashDetailsList.value?.any { it.typeName == cash.typeName } == true) {
            Toast.makeText(
                currentContext, "Cash Account with the same name already exists", Toast.LENGTH_SHORT
            ).show()
            return
        }

        viewModelScope.launch {
            try {
                firestoreRepository.addWalletItems(
                    userId,
                    "Cash",
                    cash,
                    cash.typeName,
                    onSuccess = {
                        /*dbHelper.addNewCashDetails(
                            type = cash.type,
                            typeName = cash.typeName,
                            balance = cash.balance,
                            userId = userId
                        )*/
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

    //FIXED DEPOSIT
    private fun getFDAccountDetails() {
        viewModelScope.launch {
            try {
                val fdDetailsFromFirestore = firestoreRepository.readWalletItemsFromDatabase(
                    userId,
                    "FDAccount",
                    FDAccount::class.java
                )
                updateFDDetails(fdDetailsFromFirestore)
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error adding FD details", e)
            }

        }
    }

    fun addFDDetailsToDatabase(fdAccount: FDAccount) {
        viewModelScope.launch {
            try {
                firestoreRepository.addWalletItems(
                    userId,
                    "Fixed Deposit",
                    fdAccount,
                    fdAccount.bankName,
                    onSuccess = {
                        /*dbHelper.addNewCashDetails(
                            type = cash.type,
                            typeName = cash.typeName,
                            balance = cash.balance,
                            userId = userId
                        )*/
                    },
                    onFailure = { exception ->
                        Log.e(ContentValues.TAG, "Error adding FD details", exception)
                    }
                )
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error adding FD details", e)
            }
        }
    }

    private fun updateFDDetails(fdAccountList: List<FDAccount>) {
        fdAccDetailsList.postValue(fdAccountList)
    }

    //STOCK

    private fun getStockDetails(
    ) {
        viewModelScope.launch {
            try {

                val stockDetailsFromFirestore = firestoreRepository.readWalletItemsFromDatabase(
                    userId,
                    "Stock",
                    Stock::class.java
                )

                updateStockDetails(stockDetailsFromFirestore)
                updateStockTotalPrice(stockDetailsFromFirestore)

            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error getting stock details", e)
            }
        }
    }

    private fun updateStockTotalPrice(stockList: List<Stock>) {
        var totalPrice = 0.0

        for (stock in stockList) {
            totalPrice += stock.originalPrice * stock.quantity
        }

        totalPriceStock.postValue(totalPrice)
    }

    fun addStockDetailsToDatabase(stock: Stock) {
        if (stockListLive.value?.any { it.productName == stock.productName } == true) {
            Toast.makeText(
                currentContext, "The product already existed", Toast.LENGTH_SHORT
            ).show()
            return
        }

        viewModelScope.launch {
            try {
                firestoreRepository.addWalletItems(
                    userId,
                    "Stock",
                    stock,
                    stock.productName,
                    onSuccess = {
                        /*dbHelper.addNewCashDetails(
                            type = cash.type,
                            typeName = cash.typeName,
                            balance = cash.balance,
                            userId = userId
                        )*/
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

    private suspend fun getProductName(stock: Stock): String {
        return firestoreRepository.getDocumentId("Stock", userId, stock)
    }

    fun editStockDetails(stock: Stock, updatedStockDetails: Stock) {
        /*if (stockListLive.value?.any { it.productName == stock.productName } == false) {
            Toast.makeText(
                currentContext, "The product has not added yet, add one", Toast.LENGTH_SHORT
            ).show()
            return
        }*/

        viewModelScope.launch {
            try {
                val productName = getProductName(stock)

                firestoreRepository.updateWalletItemsInFirestoreByName(
                    userId,
                    "Stock",
                    productName,
                    updatedStockDetails,
                    onSuccess = {
                        /*dbHelper.updateCashDetails(
                            updatedCashDetails.type,
                            typeName,
                            updatedCashDetails.balance,
                            userId
                        )*/

                        val stockInfo = stockListLive.value ?: emptyList()
                        val updatedStockDetailsList = stockInfo.map {
                            if (it == stock) updatedStockDetails else it
                        }

                        updateStockDetails(
                            stockList = updatedStockDetailsList
                        )

                    }
                )
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error editing stock details", e)
            }
        }
    }

    fun updateStockDetails(stockList: List<Stock>) {
        stockListLive.postValue(stockList)
    }
}