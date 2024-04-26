package com.example.spendsavvy.repo


import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.example.spendsavvy.models.Category
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import java.security.Timestamp
import java.util.Date
import java.util.UUID

class FirestoreRepository {
    private val firestore = FirebaseFirestore.getInstance()


    fun addItem(
        userId: String,
        collectionName: String,
        item: Any,
        itemIdFormat: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        val documentRef = firestore.collection("Users").document(userId).collection(collectionName)

        documentRef
            .orderBy(FieldPath.documentId(), Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { querySnapshot ->
                var latestId = 0

                // If there are documents, parse the latest ID
                if (!querySnapshot.isEmpty) {
                    val latestDocument = querySnapshot.documents[0]
                    val latestDocumentId = latestDocument.id
                    // Extract the numeric part of the document ID
                    latestId = latestDocumentId.substring(2).toIntOrNull() ?: 0
                }

                // Generate the new document ID
                val newId = itemIdFormat.format(latestId + 1)

                // Create a new document reference with the generated ID
                val newDocumentRef = documentRef.document(newId)

                // Set the category data for the new document
                newDocumentRef.set(item)
                    .addOnSuccessListener {
                        Log.d(
                            TAG,
                            "DocumentSnapshot successfully written with ID: $newId"
                        )
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        onFailure(e)
                    }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents", e)
            }


    }

    fun deleteItemFromFirestoreBySearhing(
        userId: String,
        collectionName: String,
        fieldName: String,
        value: String
    ) {
        val documentRef = firestore.collection("Users").document(userId).collection(collectionName)

        documentRef.whereEqualTo(fieldName, value)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // Delete the document using its ID
                    document.reference.delete()
                        .addOnSuccessListener {
                            Log.d(TAG, "Document successfully deleted from Firestore")
                        }
                        .addOnFailureListener { e ->
                            Log.d(TAG, "Error deleting document from Firestore: $e")
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "Error querying document in Firestore: $e")
            }
    }

    fun deleteItemFromFirestoreById(
        userId: String,
        collectionName: String,
        documentId: String
    ) {
        val documentRef = firestore.collection("Users").document(userId).collection(collectionName).document(documentId)

        documentRef.delete()
            .addOnSuccessListener {
                Log.d(TAG, "Document successfully deleted from Firestore")
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "Error deleting document from Firestore: $e")
            }
    }

    fun uploadImageToStorage(
        storageRef: StorageReference,
        imageUri: Uri,
        onSuccess: (Uri) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val uniqueImageName = UUID.randomUUID().toString()
        val imageRef = storageRef.child("images/$uniqueImageName.jpg")

        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                onSuccess(downloadUri!!)
            } else {
                onFailure(task.exception!!)
            }
        }
    }

    suspend fun <T : Any> readItemsFromDatabase(
        userId: String,
        collectionName: String,
        itemClass: Class<T>,

        ): List<T> {
        val firestore = FirebaseFirestore.getInstance()
        val itemList = mutableListOf<T>()

        try {
            val querySnapshot = firestore.collection("Users").document(userId)
                .collection(collectionName).get().await()
            for (document in querySnapshot.documents) {
                val itemData = document.data

                val item = itemClass.newInstance()
                // Set the values of item fields
                if (itemData != null) {

                    for ((fieldName, value) in itemData) {
                        try {

                            val field = itemClass.getDeclaredField(fieldName)
                            field.isAccessible = true

                            // Check if the field is the category field
                            when (fieldName) {
                                "category" -> {
                                    val categoryMap =
                                        value as Map<*, *> // Assuming the category is stored as a Map<String, Any>
                                    val category = Category(
                                        categoryName = categoryMap["categoryName"] as String ,
                                        categoryType = categoryMap["categoryType"] as String ,
                                        imageUri = categoryMap["imageUri"].let { Uri.parse(it as String) }
                                    )
                                    field.set(item, category)
                                }
                                "imageUri" -> {
                                    val imageUriString = value as String
                                    val imageUri =
                                        if (imageUriString.isNotEmpty()) Uri.parse(imageUriString) else null
                                    field.set(item, imageUri)
                                }
                                "date" -> {
                                    val date = document.getDate("date") ?: Date()
                                    field.set(item, date)
                                }
                                else -> {
                                    field.set(item, value)
                                }
                            }

                        } catch (e: NoSuchFieldException) {
                            Log.e(TAG, "Field '$fieldName' not found in $itemClass", e)
                        }
                    }
                }
                item.let { itemList.add(it) }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting items from $collectionName", e)
        }

        return itemList
    }

    suspend fun getDocumentId(collectionName: String, userId: String, data: Any): String {
        try {
            val querySnapshot = firestore.collection("Users").document(userId)
                .collection(collectionName).get().await()

            for (document in querySnapshot.documents) {
                val itemData = document.data

                val item = data::class.java.newInstance()
                // Set the values of item fields
                if (itemData != null) {

                    for ((fieldName, value) in itemData) {
                        try {

                            val field = item::class.java.getDeclaredField(fieldName)
                            field.isAccessible = true

                            // Check if the field is the category field
                            when (fieldName) {
                                "category" -> {
                                    val categoryMap =
                                        value as Map<*, *> // Assuming the category is stored as a Map<String, Any>
                                    val category = Category(
                                        categoryName = categoryMap["categoryName"] as? String ?: "",
                                        categoryType = categoryMap["categoryType"] as? String ?: "",
                                        imageUri = categoryMap["imageUri"]?.let { Uri.parse(it as String) }
                                    )
                                    field.set(item, category)
                                }
                                "imageUri" -> {
                                    val imageUriString = value as String
                                    val imageUri =
                                        if (imageUriString.isNotEmpty()) Uri.parse(imageUriString) else null
                                    field.set(item, imageUri)
                                }
                                "date" -> {
                                    val date = document.getDate("date") ?: Date()
                                    field.set(item, date)
                                }
                                else -> {
                                    field.set(item, value)
                                }
                            }

                        } catch (e: NoSuchFieldException) {
                            Log.e(TAG, "Field '$fieldName' not found in ${data::class.java}", e)
                        }
                    }
                    if (data == item) {
                        return document.id
                    }
                }



            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting document ID from $collectionName", e)
        }
        return ""
    }

    fun updateItemInFirestoreById(
        userId: String,
        collectionName: String,
        documentId: String,
        item: Any
    ) {
        val documentRef = firestore.collection("Users").document(userId).collection(collectionName).document(documentId)

        documentRef.set(item)
            .addOnSuccessListener {
                Log.d(TAG, "Document successfully updated in Firestore")
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "Error updating document in Firestore: $e")
            }
    }


}

