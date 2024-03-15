package com.example.spendsavvy

sealed class Screen(val route: String) {

    object Login : Screen(route = "login_screen")
    object SignUp : Screen(route = "signUp_screen")
}