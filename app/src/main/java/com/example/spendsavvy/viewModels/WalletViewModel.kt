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
    val FDAccountList = MutableLiveData<List<FDAccount>>()      //hold on
    val stockListLive = MutableLiveData<List<Stock>>()

    //stock
    val totalPriceStock = MutableLiveData<Double>()

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

                updateCashInfo(cashDetailsFromFirestore)

            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error getting cash details", e)
            }
        }
    }

    private fun updateCashInfo(cash: List<Cash>){
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
                        /*dbHelper.updateCashDetails(
                            updatedCashDetails.type,
                            typeName,
                            updatedCashDetails.balance,
                            userId
                        )*/

                        val cashInfo = cashDetailsList.value?: emptyList()
                        val updatedCashDetailsList = cashInfo.map{
                            if(it == cash) updatedCashDetails else it
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

    fun addCashDetailsToDatabase(cash: Cash){
        viewModelScope.launch {
            try {
                firestoreRepository.addWalletItems(
                    userId,
                    "Cash",
                    cash,
                    "%s",
                    onSuccess = {
                        /*dbHelper.addNewCashDetails(
                            type = cash.type,
                            typeName = cash.typeName,
                            balance = cash.balance,
                            userId = userId
                        )*/
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

    private fun getStockDetails(
    ) {
        viewModelScope.launch {
            try{

                val stockDetailsFromFirestore = firestoreRepository.readWalletItemsFromDatabase(
                    userId,
                    "Stock",
                    Stock::class.java
                )

                updateStockDetails(stockDetailsFromFirestore)

            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error getting stock details", e)
            }
        }
    }

    private  fun updateStockDetails(stockList: List<Stock>){
        var totalPrice = 0.0

        for (stock in stockList){
            totalPrice += stock.originalPrice * stock.quantity
        }

        stockListLive.postValue(stockList)
        totalPriceStock.postValue(totalPrice)
    }

    fun addStockDetailsToDatabase(stock: Stock){
        viewModelScope.launch {
            try {
                firestoreRepository.addWalletItems(
                    userId,
                    "Stock",
                    stock,
                    "S%04d",
                    onSuccess = {
                        /*dbHelper.addNewCashDetails(
                            type = cash.type,
                            typeName = cash.typeName,
                            balance = cash.balance,
                            userId = userId
                        )*/
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

    private suspend fun getProductName(stock: Stock): String {
        return firestoreRepository.getDocumentId("Stock", userId, stock)
    }

    fun editStockDetails(stock: Stock, updatedStockDetails: Stock){
        viewModelScope.launch{
            try{
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

                        val stockInfo = stockListLive.value?: emptyList()
                        val updatedStockDetailsList = stockInfo.map{
                            if(it == stock) updatedStockDetails else it
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

}