package com.example.spendsavvy.models

import java.util.Date

data class Transactions(
    val amount: Double,
    val category: Category,
    val description: String,
    val date: Date,
    val transactionType : String
) {
}