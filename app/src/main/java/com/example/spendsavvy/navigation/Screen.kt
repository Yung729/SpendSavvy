package com.example.spendsavvy.navigation

sealed class Screen(val route: String) {

    object Login : Screen(route = "login_screen")
    object SignUp : Screen(route = "signUp_screen")
    object Overview : Screen(route = "overview_screen")
    object Wallet : Screen(route = "wallet_screen")
    object Profile : Screen(route = "profile_screen")
    object Settings : Screen(route = "settings_screen")
    object Analysis : Screen(route = "analysis_screen")


}