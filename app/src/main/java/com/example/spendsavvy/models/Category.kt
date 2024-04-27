package com.example.spendsavvy.models

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.Exclude
import java.io.Serializable

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = false)
    @get:Exclude // Exclude from Firestore serialization
    val roomId: String? = null,
    var imageUri: Uri? ,
    var categoryName: String ,
    var categoryType: String
):Serializable  {
    constructor() : this("",null, "", "")

}