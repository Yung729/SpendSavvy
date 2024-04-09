package com.example.spendsavvy.navigation

import com.example.spendsavvy.R

sealed class Screen(val route: String, val iconResourceId: Int) {

    object Login : Screen(route = "login_screen", R.drawable.bar_chart)
    object SignUp : Screen(route = "signUp_screen", R.drawable.bar_chart)
    object Overview : Screen(route = "Overview", R.drawable.bar_chart)
    object Wallet : Screen(route = "Wallet", R.drawable.wallet)

    object Settings : Screen(route = "Setting", R.drawable.settings_icon)
    object Analysis : Screen(route = "Analysis", R.drawable.analysis_icon)
    object Stock : Screen(route = "Stock", R.drawable.bar_chart)
    object Category : Screen(route = "Category", R.drawable.bar_chart)
    object MainScreen : Screen(route = "EnterMainScreen", R.drawable.bar_chart)

    object ChangeProfile : Screen(route = "editProfile", R.drawable.edit_profile_icon)
    object MyProfile : Screen(route = "myProfile", R.drawable.profile_icon)

    object ChangePassword : Screen(route = "changePassword", R.drawable.changepassword_icon)
    object ForgotPassword : Screen(route = "forgotPassword", R.drawable.forgotpassword_icon)
    object Notifications : Screen(route = "Notifications", R.drawable.bell_icon)
    object Language : Screen(route = "Language", R.drawable.language_icon)
    object HelpAndSupport : Screen(route = "HelpAndSupport", R.drawable.help_icon)
    object ManageBillsAndInstalment : Screen(route = "ManageBillsAndInstalment", R.drawable.bills_icon)
    object TaxCalculator : Screen(route = "TaxCalculator", R.drawable.calculator_icon)
    object ManageCategory : Screen(route = "ManageCategory", R.drawable.category_icon)



}