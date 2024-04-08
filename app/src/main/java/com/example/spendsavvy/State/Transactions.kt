package com.example.spendsavvy.State

data class Transactions(
    val amount: Double,
    val category: Category,
    val description: String,
    val date: String
) {
}