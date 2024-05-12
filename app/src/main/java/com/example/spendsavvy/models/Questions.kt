package com.example.spendsavvy.models

import java.io.Serializable
import java.util.Date

data class Question(
    val id: String,
    val questionText: String,
    val answer: String,
    val status: String,
    val questionDate: Date
): Serializable {
    constructor() : this("","", "", "",Date())
}