package com.example.spendsavvy.navigation

import com.example.spendsavvy.R

sealed class Screen(val route: String, val iconResourceId: Int) {

    object Login : Screen(route = "login_screen",R.drawable.bar_chart)
    object SignUp : Screen(route = "signUp_screen",R.drawable.bar_chart)
    object Overview : Screen(route = "overview_screen", R.drawable.bar_chart)
    object Wallet : Screen(route = "wallet_screen",R.drawable.wallet)

    object Settings : Screen(route = "settings_screen",R.drawable.settings_icon)
    object Analysis : Screen(route = "analysis_screen",R.drawable.analysis_icon)
    object Stock : Screen(route = "stock_screen",R.drawable.bar_chart)
    object MainScreen : Screen(route = "EnterMainScreen",R.drawable.bar_chart)


}