package com.example.spendsavvy.viewModels

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.UserData
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel(userId: String) : ViewModel() {


    val db = FirebaseFirestore.getInstance()
    val currentUser = userId
    var userData = MutableLiveData<UserData>()

    init {
        getUserData {  user ->
            userData.value = user
        }
    }
    fun getUserData(userId: String = currentUser, onDataReceived: (UserData) -> Unit) {

        val docRef = db.collection("Users").document(userId)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val userData = document.toObject(UserData::class.java)
                    if (userData != null) {
                        onDataReceived(userData)
                    }
                }
            }
            .addOnFailureListener { e ->
                // Handle failure
                Log.w(ContentValues.TAG, "Error getting user data", e)
            }
    }

}
