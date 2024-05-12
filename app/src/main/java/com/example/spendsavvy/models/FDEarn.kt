package com.example.spendsavvy.models

import java.io.Serializable

data class FDEarn(
    val startDate: Int, //need to change to date
    val totalAmount: Double
) : Serializable {
    constructor() : this(0,0.0)
}