package com.example.spendsavvy.models

import java.io.Serializable

data class Staff(
    var id: String,
    var name: String,
    var salary: Double,
) : Serializable {
    constructor() : this("", "", 0.0)
}