package com.example.spendsavvy.models

import java.util.Date

data class Transactions(
    val amount: Double = 0.0,
    val category: Category = Category(),
    val description: String = "",
    val date: Date = Date(),
    val transactionType: String = ""
) {
}