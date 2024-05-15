package com.example.spendsavvy.models

import android.net.Uri
import java.io.Serializable

data class Cash(
    var imageUri: Uri?,
    val typeName: String,
    var balance: Double
): Serializable {
    constructor() : this(null,"",0.0)

}