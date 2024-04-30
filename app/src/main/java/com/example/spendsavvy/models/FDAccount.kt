package com.example.spendsavvy.models

import java.time.Duration

data class FDAccount(
    //@DrawableRes val BankImages: Int,
    val bankName: String,
    val duration: Duration,
    val interestRate: Double,
    val deposit: Double
){
}