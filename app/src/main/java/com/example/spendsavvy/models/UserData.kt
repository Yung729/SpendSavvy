package com.example.spendsavvy.models

import android.net.Uri

data class UserData(
    var imageUrl: Uri? = null,
    val userName : String?,
    val email : String?,
    val password : String?
){
    constructor() : this(null, null,null,null)
}
