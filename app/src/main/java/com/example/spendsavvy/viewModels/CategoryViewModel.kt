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
import java.util.UUID


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
    private val categoryList = MutableLiveData<List<Category>>()

    private val currentUserId = userId

    init {
        getCategoriesList()
    }


     fun getCategoriesList(userId: String = currentUserId) {
        viewModelScope.launch {
            try {
                val categoriesFromDB: List<Category> = if (internet) {
                    firestoreRepository.readItemsFromDatabase(
                        userId, "Categories", Category::class.java
                    )
                } else {
                    // Fetch data from SQLite
                    dbHelper.readCategory(userId)

                }

                updateCategories(categoriesFromDB)
            } catch (e: Exception) {
                Log.e(TAG, "Error getting categories", e)
            }
        }
    }

    private fun updateCategories(categories: List<Category>) {
        viewModelScope.launch {
            val groupedCategories = categories.groupBy { it.categoryType }

            val expenseCategories = groupedCategories["Expenses"] ?: emptyList()
            val incomeCategories = groupedCategories["Incomes"] ?: emptyList()

            categoryList.postValue(categories)
            expensesList.postValue(expenseCategories)
            incomeList.postValue(incomeCategories)
        }
    }



    private suspend fun getCategoryId(category: Category): String {
        return firestoreRepository.getDocumentId("Categories", currentUserId, category)
    }

    fun editCategory(category: Category, updatedCategory: Category) {
        viewModelScope.launch {
            try {
                val categoryId: String = getCategoryId(category)


                firestoreRepository.updateItemInFirestoreById(currentUserId,
                    "Categories",
                    categoryId,
                    updatedCategory,
                    onSuccess = {
                        dbHelper.updateCategory(
                            categoryId,
                            updatedCategory.id,
                            updatedCategory.imageUri,
                            updatedCategory.categoryName,
                            updatedCategory.categoryType,
                            currentUserId
                        )
                        val currentCategories = categoryList.value ?: emptyList()
                        val updatedCategories = currentCategories.map {
                            if (it == category) updatedCategory else it
                        }
                        updateCategories(categories = updatedCategories)
                        Toast.makeText(
                            currentContext, "Category edited", Toast.LENGTH_SHORT
                        ).show()
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



                firestoreRepository.deleteItemFromFirestoreById(currentUserId,
                    "Categories",
                    categoryId,
                    onSuccess = {
                        dbHelper.deleteCategory(
                            categoryId,
                            currentUserId
                        )
                        val currentCategories = categoryList.value ?: emptyList()
                        val updatedCategories = currentCategories.filter { it != category }
                        updateCategories(categories = updatedCategories)
                        Toast.makeText(
                            currentContext, "Category deleted", Toast.LENGTH_SHORT
                        ).show()
                    })


            } catch (e: Exception) {
                Log.e(TAG, "Error deleting category", e)
            }
        }
    }

    fun initializeCategoryToFirestore(userId: String) {

        val categoryData = CategoryData().loadCategory()
        val currentCategories = categoryList.value ?: emptyList()
        var updatedCategories = currentCategories

        firestoreRepository.addItemList(userId, "Categories", categoryData, "CT%04d",

            onSuccess = { documentIdList ->

                if (documentIdList.isNotEmpty()) { // Check if the list is not empty
                    categoryData.forEachIndexed { index, category ->
                        val documentId =
                            documentIdList[index % documentIdList.size] // Use modulo operator to cycle through the documentIdList
                        dbHelper.addNewCategory(
                            documentId,
                            category.id,
                            category.imageUri,
                            category.categoryName,
                            category.categoryType,
                            userId
                        )
                        updatedCategories = currentCategories + category
                    }

                    updateCategories(categories = updatedCategories)
                } else {
                    Log.e(TAG, "Document ID list is empty")
                }
            },
            onFailure = { exception ->
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

            firestoreRepository.uploadImageToStorage("images",storageRef, imageUri, { downloadUri ->
                category.imageUri = downloadUri
                firestoreRepository.addItem(currentUserId, "Categories", category, "CT%04d",

                    onSuccess = { documentId ->
                        dbHelper.addNewCategory(
                            documentId,
                            category.id,
                            category.imageUri,
                            category.categoryName,
                            category.categoryType,
                            currentUserId
                        )
                        val currentCategories = categoryList.value ?: emptyList()
                        val updatedCategories = currentCategories + category
                        updateCategories(categories = updatedCategories)
                        Toast.makeText(
                            currentContext, "Category added", Toast.LENGTH_SHORT
                        ).show()

                    }, onFailure = { exception ->
                        Log.e(TAG, "Error adding category", exception)
                        // Handle failure
                    })
            }, { exception ->
                Log.e(TAG, "Error uploading image", exception)
                // Handle failure
            })
        } else {
            firestoreRepository.addItem(currentUserId, "Categories", category, "CT%04d",
                onSuccess = { documentId ->
                    dbHelper.addNewCategory(
                        documentId,
                        category.id,
                        category.imageUri,
                        category.categoryName,
                        category.categoryType,
                        currentUserId
                    )
                    val currentCategories = categoryList.value ?: emptyList()
                    val updatedCategories = currentCategories + category
                    updateCategories(categories = updatedCategories)
                    Toast.makeText(
                        currentContext, "Category added", Toast.LENGTH_SHORT
                    ).show()

                }, onFailure = {


                })
        }
    }

    fun generateCategoryId(): String {
        val random = UUID.randomUUID().toString().substring(0, 9)
        return "CT$random"
    }

}


