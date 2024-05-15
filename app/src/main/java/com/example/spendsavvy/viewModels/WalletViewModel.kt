package com.example.spendsavvy.viewModels

import android.content.ContentValues
import android.content.Context
import android.net.Uri
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
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class WalletViewModel(
    context: Context,
    userId: String
) : ViewModel() {

    private val firestoreRepository = FirestoreRepository()
    private val dbHelper = DatabaseHelper(context)

    val cashDetailsList = MutableLiveData<List<Cash>>()
    val fdAccDetailsList = MutableLiveData<List<FDAccount>>()
    val stockListLive = MutableLiveData<List<Stock>>()

    val totalPriceStock = MutableLiveData<Double>()

    val userId = userId
    val currentContext = context

    val latestDeposit = MutableLiveData<Double>()

    init {
        getCashDetails()
        getFDAccountDetails()
        getStockDetails()
    }

    //Used for FD Earn
    fun withdrawalAmtApproved(deposit:Double, withdrawalAmt: Double){
        latestDeposit.postValue(deposit - withdrawalAmt)
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

    fun editCashDetails(cash: Cash, updatedCashDetails: Cash) {
        /*if (cashDetailsList.value?.any { it.typeName == cash.typeName } == false) {
            Toast.makeText(
                currentContext, "The cash account has not added yet, create one", Toast.LENGTH_SHORT
            ).show()
            return
        }*/

        viewModelScope.launch {
            try {

                firestoreRepository.updateWalletItemsInFirestoreByName(
                    userId,
                    "Cash",
                    cash.typeName,
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

    fun updateCashBalance(cashName: String, updateAmount: Double, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {

                firestoreRepository.updateWalletBalanceInFirestore(
                    userId,
                    cashName,
                    updateAmount,
                    onSuccess = { newBalance ->
                        /*dbHelper.updateCashDetails(
                            updatedCashDetails.type,
                            typeName,
                            updatedCashDetails.balance,
                            userId
                        )*/

                        val cashInfo = cashDetailsList.value ?: emptyList()
                        val updatedCashDetailsList = cashInfo.map {
                            if (it.typeName == cashName) it.copy(balance = newBalance) else it
                        }
                        updateCashInfo(cash = updatedCashDetailsList)
                        onSuccess()
                    },
                    onFailure = { errorMessage ->
                        Toast.makeText(
                            currentContext, errorMessage, Toast.LENGTH_SHORT
                        ).show()
                    }
                )


            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error editing cash details", e)
            }
        }
    }

    fun addCashDetailsToDatabase(cash: Cash, imageUri: Uri?) {
        if (cashDetailsList.value?.any { it.typeName == cash.typeName } == true) {
            Toast.makeText(
                currentContext, "Cash Account with the same name already exists", Toast.LENGTH_SHORT
            ).show()
            return
        }

        viewModelScope.launch {
            try {
                if (imageUri != null) {
                    val storageRef = FirebaseStorage.getInstance().reference

                    firestoreRepository.uploadImageToStorage("cashImages", storageRef, imageUri,
                        { downloadUri ->
                            cash.imageUri = downloadUri

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

                                    val cashInfo = cashDetailsList.value ?: emptyList()
                                    val updatedCashDetailsList = cashInfo + cash
                                    updateCashInfo(
                                        cash = updatedCashDetailsList
                                    )
                                },
                                onFailure = { exception ->
                                    Log.e(ContentValues.TAG, "Error adding cash details", exception)
                                }
                            )
                        }, { exception ->
                            Log.e(ContentValues.TAG, "Error uploading image", exception)
                            // Handle failure
                        })
                } else {
                    
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

                            val cashInfo = cashDetailsList.value ?: emptyList()
                            val updatedCashDetailsList = cashInfo + cash
                            updateCashInfo(
                                cash = updatedCashDetailsList
                            )
                        },
                        onFailure = { exception ->
                            Log.e(ContentValues.TAG, "Error adding cash details", exception)
                        }
                    )
                }
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error adding cash details", e)
            }
        }
        Toast.makeText(
            currentContext,
            "The cash account , ${cash.typeName} , has successfully added",
            Toast.LENGTH_SHORT
        ).show()

    }

    //FIXED DEPOSIT
    private fun getFDAccountDetails() {
        viewModelScope.launch {
            try {
                val fdDetailsFromFirestore = firestoreRepository.readWalletItemsFromDatabase(
                    userId,
                    "Fixed Deposit",
                    FDAccount::class.java
                )

                updateFDDetails(fdDetailsFromFirestore)

            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error adding FD details", e)
            }

        }
    }

    fun addFDDetailsToDatabase(fdAccount: FDAccount, imageUri: Uri?) {
        if (fdAccDetailsList.value?.any { it.bankName == fdAccount.bankName } == true) {
            Toast.makeText(
                currentContext,
                "The FD Account with the same name already existed",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        viewModelScope.launch {
            try {
                if (imageUri != null) {
                    val storageRef = FirebaseStorage.getInstance().reference

                    firestoreRepository.uploadImageToStorage("fdImages", storageRef, imageUri,
                        { downloadUri ->
                            fdAccount.imageUri = downloadUri
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
                                    val fdInfo = fdAccDetailsList.value ?: emptyList()
                                    val updatedFDDetailsFromFirestore = fdInfo + fdAccount
                                    updateFDDetails(fdAccountList = updatedFDDetailsFromFirestore)
                                },
                                onFailure = { exception ->
                                    Log.e(ContentValues.TAG, "Error adding FD details", exception)
                                }
                            )

                        }, { exception ->
                            Log.e(ContentValues.TAG, "Error uploading image", exception)
                            // Handle failure
                        })
                } else {
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
                            val fdInfo = fdAccDetailsList.value ?: emptyList()
                            val updatedFDDetailsFromFirestore = fdInfo + fdAccount
                            updateFDDetails(fdAccountList = updatedFDDetailsFromFirestore)
                        },
                        onFailure = { exception ->
                            Log.e(ContentValues.TAG, "Error adding FD details", exception)
                        }
                    )
                }
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error adding FD details", e)
            }
        }

        Toast.makeText(
            currentContext,
            "The FD Account , ${fdAccount.bankName} , has successfully added",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun editFDDetails(fdAccount: FDAccount, updatedFDDetails: FDAccount) {

        viewModelScope.launch {
            try {

                firestoreRepository.updateWalletItemsInFirestoreByName(
                    userId,
                    "Fixed Deposit",
                    fdAccount.bankName,
                    updatedFDDetails,
                    onSuccess = {


                        val fdInfo = fdAccDetailsList.value ?: emptyList()
                        val updatedFDInfo = fdInfo.map {
                            if (it == fdAccount) updatedFDDetails else it
                        }

                        updateFDDeposit(updatedFDInfo)

                    }
                )
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error editing stock details", e)
            }
        }
    }

    private fun updateFDDeposit(fdAccountList: List<FDAccount>) {
        fdAccDetailsList.postValue(fdAccountList)
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

                updateStockTotalPrice(stockDetailsFromFirestore)

            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error getting stock details", e)
            }
        }
    }

    private fun updateStockTotalPrice(stockList: List<Stock>) {
        var totalPrice = 0.00

        for (stock in stockList) {
            totalPrice += stock.originalPrice * stock.quantity
        }

        stockListLive.postValue(stockList)
        totalPriceStock.postValue(totalPrice)
    }

    fun addStockDetailsToDatabase(stock: Stock, imageUri: Uri?) {
        if (stockListLive.value?.any { it.productName == stock.productName } == true) {
            Toast.makeText(
                currentContext, "The product already existed, try another", Toast.LENGTH_SHORT
            ).show()
            return
        }

        viewModelScope.launch {
            try {
                if (imageUri != null) {
                    val storageRef = FirebaseStorage.getInstance().reference

                    firestoreRepository.uploadImageToStorage("stockImages", storageRef, imageUri,
                        { downloadUri ->
                            stock.imageUri = downloadUri
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

                                    val stockInfo = stockListLive.value ?: emptyList()
                                    val updatedStockDetailsList = stockInfo + stock
                                    updateStockTotalPrice(updatedStockDetailsList)
                                },
                                onFailure = { exception ->
                                    Log.e(
                                        ContentValues.TAG,
                                        "Error adding stock details",
                                        exception
                                    )
                                }
                            )
                        }, { exception ->
                            Log.e(ContentValues.TAG, "Error uploading image", exception)
                            // Handle failure
                        })

                } else {

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

                            val stockInfo = stockListLive.value ?: emptyList()
                            val updatedStockDetailsList = stockInfo + stock
                            updateStockTotalPrice(updatedStockDetailsList)
                        },
                        onFailure = { exception ->
                            Log.e(ContentValues.TAG, "Error adding stock details", exception)
                        }
                    )
                }
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error adding stock details", e)
            }

        }
        Toast.makeText(
            currentContext, "The product, ${stock.productName} , has successfully added", Toast.LENGTH_SHORT
        ).show()

        return
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

                firestoreRepository.updateWalletItemsInFirestoreByName(
                    userId,
                    "Stock",
                    stock.productName,
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

                        updateStockTotalPrice(updatedStockDetailsList)

                    }
                )
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error editing stock details", e)
            }
        }
    }

    fun convertDateIntoMillisecond(date: String): Long {
        val formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
        val localDate = LocalDateTime.parse(date, formatter)
        val setDayInMilliseconds = localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()

        val todayDate = (Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time).toString()
        val todayLocalDate = LocalDateTime.parse(todayDate, formatter)
        val todayInMilliseconds = todayLocalDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()

        val millisecondsDifference = todayInMilliseconds - setDayInMilliseconds

        val daysDifference = (millisecondsDifference / (1000 * 60 * 60 * 24))

        return daysDifference
    }
}