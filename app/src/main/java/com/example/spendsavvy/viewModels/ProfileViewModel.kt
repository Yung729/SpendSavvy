package com.example.spendsavvy.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spendsavvy.db.DatabaseHelper
import com.example.spendsavvy.models.UserData
import com.example.spendsavvy.repo.FirestoreRepository

class ProfileViewModel(userId: String,isConnected : Boolean,context: Context) : ViewModel() {

    val firestoreRepository = FirestoreRepository()
    val currentUser = userId
    var userData = MutableLiveData<UserData>()
    private val internet = isConnected
    private val dbHelper = DatabaseHelper(context)

    init {
        getUserData()
    }

    fun getUserData(userId: String = currentUser) {

        if (internet){
            firestoreRepository.readUser(userId,
                onSuccess = { user ->
                    userData.value = user
                },
                onFailure = { e ->
                    Log.e("ProfileViewModel", "Error getting user data", e)
                }
            )
        }else{
            userData.value = dbHelper.readUser(currentUser)
        }


    }

}
