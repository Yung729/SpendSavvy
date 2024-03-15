package com.example.spendsavvy

sealed class Screen(val route : String){
    object Login: Screen(route = "")
    object SignUp: Screen()
}