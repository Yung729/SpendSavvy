package com.example.spendsavvy.models

import java.io.Serializable
import java.time.LocalDate
import java.util.Date
data class Bills (
    val id: String ,
    val amount: Double ,
    val category: Category ,
    val description: String,
    val selectedDueDate: Date,
    val billsStatus : String
): Serializable {
    constructor() : this("",0.0, Category(), "",Date(), "")
}
