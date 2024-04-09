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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class CategoryViewModel : ViewModel() {


    private val _uiState = MutableStateFlow(Category())
    val uiState: StateFlow<Category> = _uiState.asStateFlow()


    fun addCategoryToDatabase(category: Category) {
        val database = Firebase.database
        val newCategoryRef = database.getReference("Categories")

        val categoryKey = newCategoryRef.push().key
        if (categoryKey != null) {
            val cat = Category(category.imageUri, categoryName = category.categoryName, isExpenses = category.isExpenses)
            val categoryNode = newCategoryRef.child(categoryKey)

            categoryNode.setValue(cat)
                .addOnSuccessListener {
                    Log.d(TAG, "Category added successfully.")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Failed to add category: $e")
                }
        } else {
            Log.e(TAG, "Failed to generate a key for the category.")
        }
    }

    fun readCategoriesFromDatabase(): StateFlow<List<Category>> {
        val categoryListState = MutableStateFlow<List<Category>>(emptyList())

        Firebase.database.getReference("Categories")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val categoryList = mutableListOf<Category>()
                    for (childSnapshot in snapshot.children) {
                        val category = childSnapshot.getValue(Category::class.java)
                        category?.let {
                            categoryList.add(it)
                        }
                    }
                    categoryListState.value = categoryList
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Failed to read categories: ${error.message}")
                }
            })

        return categoryListState
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