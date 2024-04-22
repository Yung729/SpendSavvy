package com.example.spendsavvy.models

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "categories")
data class Category(
    var imageUri: Uri? = null,
    var categoryName: String = "",
    var categoryType: String = ""
) : Serializable {
}