package com.example.spendsavvy.models

import java.io.Serializable
import java.time.Duration

data class FDAccount(
    //@DrawableRes val BankImages: Int,
    val bankName: String,
    val interestRate: Double,
    val deposit: Double
) : Serializable {
    constructor() : this("",0.0,0.0)
}