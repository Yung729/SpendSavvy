package com.example.spendsavvy.viewModels

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendsavvy.data.CategoryData
import com.example.spendsavvy.db.DatabaseHelper
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.repo.FirestoreRepository
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch


class CategoryViewModel(
    context: Context, isOnline: Boolean
) : ViewModel() {

    private val firestoreRepository = FirestoreRepository()
    private val dbHelper = DatabaseHelper(context)

    @SuppressLint("StaticFieldLeak")
    val currentContext = context
    private val internet = isOnline

    val expensesList = MutableLiveData<List<Category>>()
    val incomeList = MutableLiveData<List<Category>>()
    val categoryList = MutableLiveData<List<Category>>()

    val userId = "JqPinxCQzIV5Tcs9dKxul6h49192"

    private fun getCategoriesList() {
        viewModelScope.launch {
            try {
                if (internet) {
                    val categoriesFromFirestore = firestoreRepository.readItemsFromDatabase(
                        userId, "Categories", Category::class.java
                    )

                    updateCategories(categoriesFromFirestore)

                    dbHelper.deleteAllCategories()
                    // Update SQLite with Firestore data
                    dbHelper.resetPrimaryKey("categories")

                    categoriesFromFirestore.forEach { category ->
                        dbHelper.addNewCategory(
                            category.imageUri, category.categoryName, category.categoryType
                        )
                    }
                    Log.e(TAG, "Have Internet")
                } else {
                    // Fetch data from SQLite
                    val categoriesFromSQLite = dbHelper.readCategory()
                    updateCategories(categoriesFromSQLite)
                    Log.e(TAG, "No Internet")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting categories", e)
            }
        }
    }

    private fun updateCategories(categories: List<Category>) {
        val expenseCategories = mutableListOf<Category>()
        val incomeCategories = mutableListOf<Category>()

        categories.forEach { category ->
            if (category.categoryType == "Expenses") {
                expenseCategories.add(category)
            } else if (category.categoryType == "Incomes") {
                incomeCategories.add(category)
            }
        }

        categoryList.postValue(categories)
        expensesList.postValue(expenseCategories)
        incomeList.postValue(incomeCategories)
    }

    init {
        getCategoriesList()
    }


    private suspend fun getCategoryId(category: Category): String {
        return firestoreRepository.getDocumentId("Categories", userId, category)
    }

    fun editCategory(category: Category, updatedCategory: Category) {
        viewModelScope.launch {
            try {
                val categoryId: String = getCategoryId(category)

                firestoreRepository.updateItemInFirestoreById(userId,
                    "Categories",
                    categoryId,
                    updatedCategory,
                    onSuccess = {
                        getCategoriesList()
                    })

            } catch (e: Exception) {
                Log.e(TAG, "Error editing category", e)
            }
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            try {
                val categoryId: String = getCategoryId(category)

                firestoreRepository.deleteItemFromFirestoreById(userId,
                    "Categories",
                    categoryId,
                    onSuccess = {
                        getCategoriesList()
                    })

            } catch (e: Exception) {
                Log.e(TAG, "Error deleting category", e)
            }
        }
    }

    private fun initializeCategoryToFirestore() {
        val firestore = FirebaseFirestore.getInstance()
        val categoryData = CategoryData().loadCategory()
        val documentRef = firestore.collection("Users").document(userId).collection("Categories")

        documentRef.orderBy(FieldPath.documentId(), Query.Direction.DESCENDING).limit(1).get()
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
                    newDocumentRef.set(category).addOnSuccessListener {
                        Log.d(TAG, "Category successfully written to Firestore: $newId")
                    }.addOnFailureListener { e ->
                        Log.w(TAG, "Error writing category to Firestore: $newId", e)
                    }

                    latestId++ // Increment latestId for the next category
                }
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error getting documents", e)
            }


    }


    fun addCategoryToDatabase(category: Category, imageUri: Uri?) {

        if (categoryList.value?.any { it.categoryName == category.categoryName } == true) {
            Toast.makeText(
                currentContext, "Category with the same name already exists", Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (imageUri != null) {
            val storageRef = FirebaseStorage.getInstance().reference

            firestoreRepository.uploadImageToStorage(storageRef, imageUri, { downloadUri ->
                category.imageUri = downloadUri
                firestoreRepository.addItem(userId, "Categories", category, "CT%04d", onSuccess = {
                    getCategoriesList()
                }, onFailure = { exception ->
                    Log.e(TAG, "Error adding category", exception)
                    // Handle failure
                })
            }, { exception ->
                Log.e(TAG, "Error uploading image", exception)
                // Handle failure
            })
        } else {
            firestoreRepository.addItem(userId, "Categories", category, "CT%04d", onSuccess = {
                getCategoriesList()
            }, onFailure = {


            })
        }
    }

}


