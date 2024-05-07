package com.example.spendsavvy.models

import java.io.Serializable
import java.time.LocalDate
import java.util.Date
data class Bills (
    val amount: Double ,
    val category: Category ,
    val description: String,
    val date: LocalDate ,
    val transactionType: String,
    val selectedDueDate: LocalDate?,
    val selectedDuration: String
): Serializable {
    constructor() : this(0.0, Category(), "", LocalDate.now(),"", null, "")
}
