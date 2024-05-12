package com.example.spendsavvy.repo


import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.UserData
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID

class FirestoreRepository {
    private val firestore = FirebaseFirestore.getInstance()


    fun addItem(
        userId: String,
        collectionName: String,
        item: Any,
        itemIdFormat: String,
        onSuccess: (String) -> Unit,
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
                        onSuccess(newId)
                    }
                    .addOnFailureListener { e ->
                        onFailure(e)
                    }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents", e)
            }


    }

    fun addOrUpdateOneFieldItem(
        userId: String,
        collectionName: String,
        fieldName: String,
        fieldData: Any,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        val documentRef = firestore.collection("Users").document(userId).collection(collectionName)
            .document(fieldName)

        val budgetData = hashMapOf(
            fieldName to fieldData
        )

        documentRef.set(budgetData)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written")
                onSuccess("")
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "DocumentSnapshot unsuccessfully written")
                onFailure(e)
            }


    }

    suspend fun readSingleFieldValueFromDatabase(
        userId: String,
        collectionName: String,
        fieldName: String
    ): Any? {
        try {
            val querySnapshot = firestore.collection("Users").document(userId)
                .collection(collectionName).get().await()

            for (document in querySnapshot.documents) {
                val fieldValue = document[fieldName]
                if (fieldValue != null) {
                    return fieldValue
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting items from $collectionName", e)
        }
        return null
    }


    fun addItemList(
        userId: String,
        collectionName: String,
        item: List<Any>,
        itemIdFormat: String,
        onSuccess: (List<String>) -> Unit,
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

                val newIds = mutableListOf<String>()

                for (i in item) {
                    // Generate the new document ID
                    val newId = itemIdFormat.format(latestId + 1)

                    // Create a new document reference with the generated ID
                    val newDocumentRef = documentRef.document(newId)

                    // Set the category data for the new document
                    newDocumentRef.set(i).addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot successfully written with ID: $newId")
                        newIds.add(newId)

                        if (newIds.size == item.size) {
                            // Call onSuccess only when all documents are written
                            onSuccess(newIds)
                        }

                    }.addOnFailureListener { e ->
                        Log.w(TAG, "DocumentSnapshot fail written with ID: $newId", e)
                    }

                    latestId++ // Increment latestId for the next category
                }

            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents", e)
                onFailure(e)
            }


    }


    fun deleteItemFromFirestoreById(
        userId: String,
        collectionName: String,
        documentId: String,
        onSuccess: () -> Unit
    ) {
        val documentRef = firestore.collection("Users").document(userId).collection(collectionName)
            .document(documentId)

        documentRef.delete()
            .addOnSuccessListener {
                Log.d(TAG, "Document successfully deleted from Firestore")
                onSuccess()
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
                                        id = categoryMap["id"] as String,
                                        categoryName = categoryMap["categoryName"] as String,
                                        categoryType = categoryMap["categoryType"] as String,
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
                                "selectedDueDate" -> {
                                    val date = document.getDate("selectedDueDate") ?: Date()
                                    field.set(item, date)
                                }
                                "questionDate" -> {
                                    val date = document.getDate("questionDate") ?: Date()
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


    suspend fun totalStaffSalariesPerUser(): Map<String, Double> {
        val totalSalariesPerUser = mutableMapOf<String, Double>()

        try {
            val userSnapshot = firestore.collection("Users").get().await()
            for (userDocument in userSnapshot.documents) {
                val userId = userDocument.id
                var totalSalaries = 0.0

                val staffQuerySnapshot = userDocument.reference.collection("Staff").get().await()
                for (staffDocument in staffQuerySnapshot.documents) {
                    val staffData = staffDocument.data
                    if (staffData != null) {
                        val salary = staffData["salary"] as? Double
                        totalSalaries += salary ?: 0.0
                    }
                }

                totalSalariesPerUser[userId] = totalSalaries
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting staff salaries per user", e)
        }

        return totalSalariesPerUser
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
                                        id = categoryMap["id"] as String,
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
                                "selectedDueDate" -> {
                                    val date = document.getDate("selectedDueDate") ?: Date()
                                    field.set(item, date)
                                }
                                "questionDate" -> {
                                    val date = document.getDate("questionDate") ?: Date()
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
        item: Any,
        onSuccess: () -> Unit
    ) {
        val documentRef = firestore.collection("Users").document(userId).collection(collectionName)
            .document(documentId)

        documentRef.set(item)
            .addOnSuccessListener {
                Log.d(TAG, "Document successfully updated in Firestore")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "Error updating document in Firestore: $e")
            }
    }

    //----------------------  Two Collection -----------------------------------------------------------


    suspend fun readItemsWalletCollection(
        userId: String,
        collectionName: String,
        fieldName: String
    ): Any? {
        try {
            val querySnapshot =
                firestore.collection("Users").document(userId).collection("Wallet").document("1")
                    .collection(collectionName).get().await()

            for (document in querySnapshot.documents) {
                val fieldValue = document[fieldName]
                if (fieldValue != null) {
                    return fieldValue
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error getting items from $collectionName", e)
        }
        return null
    }

    fun addWalletItems(
        userId: String,
        collectionName: String,
        item: Any,
        walletName: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        val documentRef =
            firestore.collection("Users").document(userId).collection("Wallet").document("1")
                .collection(collectionName).document(walletName)

        documentRef.set(item)
            .addOnSuccessListener {
                Log.d(
                    TAG,
                    "DocumentSnapshot successfully written with ID: $walletName"
                )
                onSuccess(walletName)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }


    }

    suspend fun <T : Any> readWalletItemsFromDatabase(
        userId: String,
        collectionName: String,
        itemClass: Class<T>,
    ): List<T> {
        val firestore = FirebaseFirestore.getInstance()
        val itemList = mutableListOf<T>()

        try {
            val querySnapshot = firestore.collection("Users").document(userId)
                .collection("Wallet").document("1").collection(collectionName).get().await()
            for (document in querySnapshot.documents) {
                val itemData = document.data

                val item = itemClass.newInstance()
                // Set the values of item fields
                if (itemData != null) {

                    for ((fieldName, value) in itemData) {
                        try {

                            val field = itemClass.getDeclaredField(fieldName)
                            field.isAccessible = true

                            field.set(item, value)

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

    fun updateWalletItemsInFirestoreByName(
        userId: String,
        collectionName: String,
        typeName: String,
        item: Any,
        onSuccess: () -> Unit
    ) {
        val documentRef =
            firestore.collection("Users").document(userId).collection("Wallet").document("1")
                .collection(collectionName).document(typeName)

        documentRef.set(item)
            .addOnSuccessListener {
                Log.d(TAG, "Document successfully updated in Firestore")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "Error updating document in Firestore: $e")
            }
    }

    // ---------------- User -----------------------------------------

    fun readUser(
        userId: String,
        onSuccess: (UserData) -> Unit,
        onFailure: (Exception) -> Unit
    ): UserData {
        val docRef = firestore.collection("Users").document(userId)
        var userData = UserData()
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    userData = document.toObject(UserData::class.java)!!
                    if (userData != null) {
                        onSuccess(userData)
                    } else {
                        onFailure(Exception("User data is null"))
                    }
                }
            }
            .addOnFailureListener { e ->
                // Handle failure
                Log.w(TAG, "Error getting user data", e)
            }

        return userData
    }


}

