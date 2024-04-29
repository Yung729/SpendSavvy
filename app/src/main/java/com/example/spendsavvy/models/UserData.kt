package com.example.spendsavvy.models

data class UserData(
    val userID: String,
    val photoURL: String?,
    var userName: String,
    var phoneNo: String,
    var email: String,
    val password: String,
){
    constructor() : this("","", "","","", "")
}
