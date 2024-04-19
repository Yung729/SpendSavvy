package com.example.spendsavvy.viewModels

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendsavvy.data.CategoryData
import com.example.spendsavvy.models.Category
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID


class CategoryViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    val expensesList = MutableStateFlow<List<Category>>(emptyList())
    val incomeList = MutableStateFlow<List<Category>>(emptyList())


    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    val userId = "JqPinxCQzIV5Tcs9dKxul6h49192"

    private fun getCategoriesList() {
        viewModelScope.launch {
            try {
                val (expenses, income) = readCategoriesFromDatabase(userId)
                expensesList.value = expenses
                incomeList.value = income
            } catch (e: Exception) {
                Log.e(TAG, "Error getting categories", e)
            }
        }
    }

    init {
        getCategoriesList()
    }


    private fun addCategoryToFirestore(category: Category) {

        val documentRef = firestore.collection("Users").document(userId).collection("Categories")

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
                val newId = "CT${"%04d".format(latestId + 1)}"

                // Create a new document reference with the generated ID
                val newDocumentRef = documentRef.document(newId)

                // Set the category data for the new document
                newDocumentRef.set(category)
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot successfully written with ID: $newId")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error writing document", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents", e)
            }


    }

    private fun initializeCategoryToFirestore() {
        val categoryData = CategoryData().loadCategory()
        val documentRef = firestore.collection("Users").document(userId).collection("Categories")

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

                for (category in categoryData) {
                    // Generate the new document ID
                    val newId = "CT${"%04d".format(latestId + 1)}"

                    // Create a new document reference with the generated ID
                    val newDocumentRef = documentRef.document(newId)

                    // Set the category data for the new document
                    newDocumentRef.set(category)
                        .addOnSuccessListener {
                            Log.d(TAG, "Category successfully written to Firestore: $newId")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error writing category to Firestore: $newId", e)
                        }

                    latestId++ // Increment latestId for the next category
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents", e)
            }


    }

    private fun uploadImageToStorage(imageUri: Uri, context: Context, onComplete: (Uri?) -> Unit) {
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
                onComplete(downloadUri)
            } else {
                Toast.makeText(context, "Image upload failed", Toast.LENGTH_SHORT).show()
                onComplete(null)
            }
        }
    }


    fun addCategoryToDatabase(category: Category, imageUri: Uri?, context: Context) {
        if (imageUri != null) {
            uploadImageToStorage(imageUri, context) { downloadUri ->
                category.imageUri = downloadUri
                addCategoryToFirestore(category)

            }
        } else {
            addCategoryToFirestore(category)
        }
    }
}

suspend fun readCategoriesFromDatabase(userId: String): Pair<List<Category>, List<Category>> {
    val firestore = FirebaseFirestore.getInstance()
    val expensesList = mutableListOf<Category>()
    val incomeList = mutableListOf<Category>()

    try {
        val querySnapshot = firestore.collection("Users").document(userId)
            .collection("Categories").get().await()
        for (document in querySnapshot.documents) {
            val imageUriString = document.getString("imageUri")
            val categoryName = document.getString("categoryName") ?: ""
            val categoryType = document.getString("categoryType") ?: ""

            val imageUri = imageUriString?.let { Uri.parse(it) }

            val category = Category(imageUri, categoryName, categoryType)
            if (categoryType == "Expenses") {
                expensesList.add(category)
            } else if (categoryType == "Incomes") {
                incomeList.add(category)
            }
        }
    } catch (e: Exception) {
        Log.e(TAG, "Error getting categories", e)
    }

    return Pair(expensesList, incomeList)
}

