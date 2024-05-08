package com.example.spendsavvy.models

import java.io.Serializable
import java.time.LocalDate
import java.util.Date
data class Bills (
    val amount: Double ,
    val category: Category ,
    val description: String,
    val selectedDueDate: String,
    val selectedDuration: String,
    val billsStatus : String
): Serializable {
    constructor() : this(0.0, Category(), "","", "", "")
}
