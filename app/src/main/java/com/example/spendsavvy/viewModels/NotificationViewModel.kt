package com.example.spendsavvy.viewModels

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotificationViewModel(context: Context) : ViewModel() {

    private val sharedPref: SharedPreferences by lazy {
        context.getSharedPreferences("notification", Context.MODE_PRIVATE)
    }

    fun isNotificationEnabled(isEnabled: Boolean) {
        saveCredentials(isEnabled)
    }

    fun getNotificationStatus(): Boolean {
        return sharedPref.getBoolean("notificationEnabled", true)
    }

    private fun saveCredentials(isEnabled: Boolean) {
        val editor = sharedPref.edit()
        editor.putBoolean("notificationEnabled", isEnabled)
        editor.apply()
    }
}

