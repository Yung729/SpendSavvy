package com.example.spendsavvy.models

import androidx.annotation.DrawableRes

data class Stock(
    //@DrawableRes val productImage : Int,
    val productName : String,
    val quantity : Int,
    val originalPrice : Double
) {
}