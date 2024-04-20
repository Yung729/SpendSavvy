package com.example.spendsavvy.viewModels

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.Transactions
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date

class OverviewViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()


    val userId = "JqPinxCQzIV5Tcs9dKxul6h49192"
    val transactionsList = MutableStateFlow<List<Transactions>>(emptyList())

    private fun getTransactionRecord() {
        viewModelScope.launch {
            try {
                val transactions = readTransactionFromDatabase(userId)
                transactionsList.value = transactions
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error getting transactions", e)
            }
        }
    }

    init {
        getTransactionRecord()
    }


    fun addTransactionToFirestore(transaction: Transactions) {
        val transactionsCollectionRef =
            firestore.collection("Users").document(userId).collection("Transactions")

        // Get the latest transaction ID to generate a new one
        transactionsCollectionRef.orderBy(FieldPath.documentId(), Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { querySnapshot ->
                var latestId = 0

                if (!querySnapshot.isEmpty) {
                    val latestDocument = querySnapshot.documents[0]
                    val latestDocumentId = latestDocument.id
                    latestId = latestDocumentId.substring(1).toIntOrNull() ?: 0
                }

                // Generate the new transaction ID
                val newTransactionId = "T${"%04d".format(latestId + 1)}"

                // Check if the category already

                // Add the transaction to Firestore
                transactionsCollectionRef.document(newTransactionId).set(transaction)
                    .addOnSuccessListener {
                        Log.d(ContentValues.TAG, "Transaction added successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "Error adding transaction", e)
                    }


            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error getting latest transaction ID", e)
            }
    }
}

suspend fun readTransactionFromDatabase(userId: String): List<Transactions> {
    val firestore = FirebaseFirestore.getInstance()
    val transactionList = mutableListOf<Transactions>()

    try {
        val querySnapshot = firestore.collection("Users").document(userId)
            .collection("Transactions").get().await()
        for (document in querySnapshot.documents) {


            val categoryMap = document.get("category") as? Map<*, *>
            val category = Category(
                categoryName = categoryMap?.get("categoryName") as? String ?: "",
                categoryType = categoryMap?.get("categoryType") as? String ?: "",
                imageUri = Uri.parse(categoryMap?.get("imageUri") as? String ?: ""),
            )
            val transaction = Transactions(
                amount = document.getDouble("amount") ?: 0.0,
                description = document.getString("description") ?: "",
                date = document.getDate("date") ?: Date(),
                transactionType = document.getString("transactionType") ?: "",
                category = category
            )

            transactionList.add(transaction)
        }
    } catch (e: Exception) {
        Log.e(ContentValues.TAG, "Error getting categories", e)
    }

    return transactionList

}