package com.example.spendsavvy.models

import java.io.Serializable
import java.time.Duration
import java.util.Date

data class FDAccount(
    //@DrawableRes val BankImages: Int,
    val bankName: String,
    val interestRate: Double,
    val deposit: Double,
    val date: Date      //store start date
) : Serializable {
    constructor() : this("",0.0,0.0, Date())
}