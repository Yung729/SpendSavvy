package com.example.spendsavvy.model

data class UserData(
    val userName : String?,
    val email : String?,
    val password : String?
){
    constructor() : this(null,null,null)
}
