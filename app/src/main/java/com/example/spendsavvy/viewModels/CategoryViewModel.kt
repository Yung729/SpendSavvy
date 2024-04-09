package com.example.spendsavvy.viewModels

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.spendsavvy.models.Category
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID


class CategoryViewModel : ViewModel() {


    private val _uiState = MutableStateFlow(Category())
    val uiState: StateFlow<Category> = _uiState.asStateFlow()

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

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

    /*fun readCategoriesFromDatabase(onSuccess: (List<Category>) -> Unit, onFailure: (Exception) -> Unit) {
        val db = Firebase.firestore

        db.collection("Categories")
            .get()
            .addOnSuccessListener { result ->
                val categoryList = mutableListOf<Category>()

                for (document in result) {
                    val category = document.toObject(Category::class.java)
                    categoryList.add(category)
                }

                onSuccess(categoryList)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }*/

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
            uploadImageToStorage(imageUri, context) {downloadUri ->
                category.imageUri = downloadUri
                addCategoryToFirestore(category)
            }
        } else {
            addCategoryToFirestore(category)
        }
    }

    fun addCategory(name: String, imageUri: Uri?, isExpenses: Boolean) {
        _uiState.update { current ->
            current.copy(
                categoryName = name,
                imageUri = imageUri,
                isExpenses = isExpenses
            )
        }
    }


}