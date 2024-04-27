package com.example.spendsavvy.repo

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.LiveData
import com.example.spendsavvy.dao.CategoryDao
import com.example.spendsavvy.models.Category

class CategoryRoomRepository(
    private val context: Context,
    private val categoryDao: CategoryDao,
    private val firestoreRepository: FirestoreRepository
) {
    val allCategories: LiveData<List<Category>> = categoryDao.getAllCategories()

    suspend fun syncCategoriesWithFirestore(userId: String) {
        if (isInternetAvailable()) {
            val categoriesFromFirestore = firestoreRepository.readItemsFromDatabase(userId, "Categories", Category::class.java)
            categoryDao.insertAll(categoriesFromFirestore)
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }
}