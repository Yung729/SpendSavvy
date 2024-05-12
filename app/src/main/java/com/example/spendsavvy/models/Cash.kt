package com.example.spendsavvy.models

import java.io.Serializable

data class Cash(
    val typeName: String,
    val balance: Double
): Serializable {
    constructor() : this("",0.0)

}