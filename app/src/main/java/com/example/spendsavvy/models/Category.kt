package com.example.spendsavvy.models

import android.net.Uri
import androidx.annotation.DrawableRes

data class Category(
    var imageUri: Uri? = null,
    @DrawableRes val imageResourceId: Int = 0,
    var categoryName: String = "",
    var isExpenses: Boolean = true
) {
}