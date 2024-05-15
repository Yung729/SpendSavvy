package com.example.spendsavvy.models

import android.net.Uri
import androidx.annotation.DrawableRes
import java.io.Serializable
import java.util.Date

data class FDAccount(
    var imageUri: Uri?,
    val bankName: String,
    val interestRate: Double,
    val deposit: Double,
    val date: Date
) : Serializable {
    constructor() : this(null,"",0.0,0.0, Date())
}