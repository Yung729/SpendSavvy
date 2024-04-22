package com.example.spendsavvy.models

import android.net.Uri
import androidx.annotation.DrawableRes
import java.io.Serializable

data class Category(
    var imageUri: Uri? = null,
    var categoryName: String = "",
    var categoryType: String = ""
) : Serializable{
}