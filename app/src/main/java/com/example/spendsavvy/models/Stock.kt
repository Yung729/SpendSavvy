package com.example.spendsavvy.models

import androidx.annotation.DrawableRes
import java.io.Serializable

data class Stock(
    //@DrawableRes val productImage : Int,
    val productName : String,
    val originalPrice : Double,
    val quantity : Int
) : Serializable {
    constructor() : this("",0.0,0)
}