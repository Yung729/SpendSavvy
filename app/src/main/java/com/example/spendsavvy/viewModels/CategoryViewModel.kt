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