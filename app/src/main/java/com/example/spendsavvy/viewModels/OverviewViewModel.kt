package com.example.spendsavvy.viewModels

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.spendsavvy.models.Transactions
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class OverviewViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()


    val userId = "JqPinxCQzIV5Tcs9dKxul6h49192"

    /*private fun getTransactionRecord() {
        viewModelScope.launch {
            try {

            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error getting categories", e)
            }
        }
    }

    init {
        getTransactionRecord()
    }*/


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

suspend fun readTransactionFromDatabase(userId: String) {
    val firestore = FirebaseFirestore.getInstance()


    try {

    } catch (e: Exception) {
        Log.e(ContentValues.TAG, "Error getting categories", e)
    }


}