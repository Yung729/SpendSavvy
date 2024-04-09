package com.example.spendsavvy.models

data class Transactions(
    val amount: Double,
    val category: Category,
    val description: String,
    val date: String
) {
}