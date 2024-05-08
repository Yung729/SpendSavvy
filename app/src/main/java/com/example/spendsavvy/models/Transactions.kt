package com.example.spendsavvy.models

import java.io.Serializable
import java.util.Date

data class Transactions(
    val id: String ,
    val amount: Double ,
    val category: Category ,
    val description: String,
    val date: Date ,
    val transactionType: String
): Serializable {
    constructor() : this("",0.0, Category(), "",Date(),"")
}