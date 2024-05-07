package com.example.spendsavvy.viewModels

import androidx.lifecycle.viewModelScope
import com.example.spendsavvy.models.Bills
import com.example.spendsavvy.models.Transactions
import kotlinx.coroutines.launch

class BillsViewModel {
    fun addBillsToFirestore(
        transaction: Bills,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
//        viewModelScope.launch {
//            try {
//                val categoryId: String = getCategoryId(transaction.category)
//
//                firestoreRepository.addItem(
//                    currentUserId,
//                    "Transactions",
//                    transaction,
//                    "T%04d",
//                    onSuccess = { documentId ->
//                        dbHelper.addNewTransaction(
//                            transactionId = documentId,
//                            amount = transaction.amount,
//                            categoryId = categoryId,
//                            description = transaction.description,
//                            date = transaction.date,
//                            transactionType = transaction.transactionType,
//                            userId = currentUserId
//                        )
//                        getTransactionRecord()
//                        onSuccess() // Invoke the onSuccess callback
//                    },
//                    onFailure = { exception ->
//                        Log.e(ContentValues.TAG, "Error adding transaction", exception)
//                        onFailure(exception) // Invoke the onFailure callback
//                    }
//                )
//            } catch (e: Exception) {
//                Log.e(ContentValues.TAG, "Error adding transaction", e)
//                onFailure(e) // Invoke the onFailure callback
//            }
//        }
//    }
    }
}