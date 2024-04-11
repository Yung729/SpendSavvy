package com.example.spendsavvy.viewModels

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendsavvy.models.Category
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID


class CategoryViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _categoryList = MutableStateFlow<List<Category>>(emptyList())
    val categoryList = MutableStateFlow<List<Category>>(emptyList())

    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    private fun getCategoriesList(){
        viewModelScope.launch {
            try {
                val categories = readCategoriesFromDatabase()
                categoryList.value = categories
            } catch (e: Exception) {
                Log.e(TAG, "Error getting categories", e)
            }
        }
    }

    init {
        getCategoriesList()
    }

    private fun addCategoryToFirestore(category: Category) {
        firestore.collection("Categories")
            .add(category)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot successfully written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing document", e)
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

suspend fun readCategoriesFromDatabase(): List<Category> {
    val firestore = FirebaseFirestore.getInstance()
    val categoryList = mutableListOf<Category>()

    try {
        val querySnapshot = firestore.collection("Categories").get().await()
        for (document in querySnapshot.documents) {
            val imageUriString = document.getString("imageUri")
            val categoryName = document.getString("categoryName") ?: ""
            val isExpenses = document.getBoolean("isExpenses") ?: true

            val imageUri = imageUriString?.let { Uri.parse(it) }

            val category = Category(imageUri, categoryName, isExpenses)
            categoryList.add(category)
        }
    } catch (e: Exception) {
        Log.e(TAG, "Error getting categories", e)
    }

    return categoryList
}