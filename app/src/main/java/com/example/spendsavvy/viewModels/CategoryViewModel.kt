package com.example.spendsavvy.viewModels

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.spendsavvy.models.Category
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class CategoryViewModel : ViewModel() {


    private val _uiState = MutableStateFlow(Category())
    val uiState: StateFlow<Category> = _uiState.asStateFlow()


    fun addCategoryToDatabase(category : Category){
        val db = Firebase.firestore

        // Add the category as a new document with an automatically generated unique ID
        db.collection("Categories")
            .add(category)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot successfully written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing document", e)
            }
    }

    fun readCategoriesFromDatabase(onSuccess: (List<Category>) -> Unit, onFailure: (Exception) -> Unit) {
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