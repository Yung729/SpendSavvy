package com.example.spendsavvy.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendsavvy.models.UserData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel: ViewModel() {
    val state = mutableStateOf(UserData())
    init {
        getData()
    }
    private fun getData(){
        viewModelScope.launch {
            state.value = getDataFromFireStone()
        }
    }
}
suspend fun getDataFromFireStone(): UserData {
    val db = FirebaseFirestore.getInstance()
    var userData = UserData()

    try{
        db.collection("Users").get().await().map {
           val result = it.toObject(UserData::class.java)
            userData = result
        }
    }catch(e: FirebaseFirestoreException){
        Log.d("error", "getDataFromFireStore: $e")

    }
    return userData
}