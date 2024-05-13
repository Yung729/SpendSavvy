package com.example.spendsavvy.models

import java.io.Serializable

data class UserData(
    val userID: String,
    val photoURL: String?,
    var userName: String,
    var phoneNo: String,
    var email: String,
    val password: String,
): Serializable {
    constructor() : this("","", "","","", "")
}
