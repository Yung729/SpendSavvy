package com.example.spendsavvy.model

import androidx.annotation.DrawableRes

data class Transactions(
    @DrawableRes val imageResourceId: Int,
    val amount: Double,
    val category: Category,
    val description: String,
    val date: String
) {
}