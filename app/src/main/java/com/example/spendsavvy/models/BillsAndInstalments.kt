package com.example.spendsavvy.models

data class Bill(
    val category: String,
    val title: String,
    val amount: Double,
    val date: String,
    val dateCategory: String // Upcoming, Paid, Overdue
)