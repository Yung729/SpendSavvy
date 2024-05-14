package com.example.spendsavvy.models

import android.net.Uri
import androidx.annotation.DrawableRes
import java.io.Serializable

data class Stock(
    var imageUri: Uri?,
    val productName : String,
    val originalPrice : Double,
    val quantity : Int
) : Serializable {
    constructor() : this(null,"",0.0,0)
}