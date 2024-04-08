package com.example.spendsavvy.State

import androidx.annotation.DrawableRes

data class Transactions(
    val amount: Double,
    val category: Category,
    val description: String,
    val date: String
) {
}