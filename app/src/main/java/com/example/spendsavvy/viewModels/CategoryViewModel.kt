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
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch


class CategoryViewModel(
    context: Context, isOnline: Boolean, userId: String
) : ViewModel() {

    private val firestoreRepository = FirestoreRepository()
    private val dbHelper = DatabaseHelper(context)

    @SuppressLint("StaticFieldLeak")
    val currentContext = context
    private val internet = isOnline

    val expensesList = MutableLiveData<List<Category>>()
    val incomeList = MutableLiveData<List<Category>>()
    val categoryList = MutableLiveData<List<Category>>()

    val userId = userId

    private fun getCategoriesList() {
        viewModelScope.launch {
            try {
                if (internet) {
                    val categoriesFromFirestore = firestoreRepository.readItemsFromDatabase(
                        userId, "Categories", Category::class.java
                    )

                    updateCategories(categoriesFromFirestore)

                    dbHelper.deleteAllCategories()
                    dbHelper.resetPrimaryKey("categories")

                    categoriesFromFirestore.forEach { category ->
                        dbHelper.addNewCategory(
                            category.imageUri, category.categoryName, category.categoryType, userId
                        )
                    }

                } else {
                    // Fetch data from SQLite
                    val categoriesFromSQLite = dbHelper.readCategory()
                    updateCategories(categoriesFromSQLite)

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

    fun initializeCategoryToFirestore(userId: String) {

        val categoryData = CategoryData().loadCategory()

        firestoreRepository.addItemList(userId, "Categories", categoryData, "CT%04d", onSuccess = {
            getCategoriesList()
        }, onFailure = { exception ->
            Log.e(TAG, "Error adding category", exception)
            // Handle failure
        })
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


