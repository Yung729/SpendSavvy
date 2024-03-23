package com.example.spendsavvy.model

import androidx.annotation.DrawableRes

data class Transactions(
    val amount: Double,
    val category: Category,
    val description: String,
    val date: String
) {
}