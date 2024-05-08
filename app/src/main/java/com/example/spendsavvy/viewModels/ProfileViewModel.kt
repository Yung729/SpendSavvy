package com.example.spendsavvy.viewModels

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.UserData
import com.example.spendsavvy.repo.FirestoreRepository
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel(userId: String) : ViewModel() {


    val firestoreRepository = FirestoreRepository()
    val currentUser = userId
    var userData = MutableLiveData<UserData>()

    init {
         getUserData()
    }
    fun getUserData(userId: String = currentUser)  {

        firestoreRepository.readUser(userId,
            onSuccess = { user ->
                userData.value = user
            },
            onFailure = { e ->
                Log.e("ProfileViewModel", "Error getting user data", e)
            }
        )

    }

}
