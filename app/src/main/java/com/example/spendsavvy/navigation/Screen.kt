package com.example.spendsavvy.navigation

import com.example.spendsavvy.R

sealed class Screen(val route: String, val iconResourceId: Int) {

    object Login : Screen(route = "login_screen", R.drawable.bar_chart)
    object SignUp : Screen(route = "signUp_screen", R.drawable.bar_chart)
    object Overview : Screen(route = "Overview", R.drawable.bar_chart)
    object AllTransaction : Screen(route = "Transaction Record", R.drawable.bar_chart)
    object TransactionDetails : Screen(route = "Transaction Details", R.drawable.bar_chart)
    object Wallet : Screen(route = "Wallet", R.drawable.wallet)

    object Settings : Screen(route = "Settings", R.drawable.settings_icon)
    object Analysis : Screen(route = "Analysis", R.drawable.analysis_icon)
    object Stock : Screen(route = "Stock", R.drawable.bar_chart)
    object Category : Screen(route = "Category", R.drawable.category_icon)
    object AddCategory : Screen(route = "Add Category", R.drawable.category_icon)
    object CategoryDetail : Screen(route = "Category Details", R.drawable.category_icon)
    object MainScreen : Screen(route = "EnterMainScreen", R.drawable.bar_chart)

    object ChangeProfile : Screen(route = "Edit Profile", R.drawable.edit_profile_icon)
    object MyProfile : Screen(route = "My Profile", R.drawable.profile_icon)

    object ChangePassword : Screen(route = "Change Password ", R.drawable.changepassword_icon)
    object ForgotPassword : Screen(route = "Forgot Password", R.drawable.forgotpassword_icon)
    object CreatePassword : Screen(route = "Create Password", 0)
    object Notifications : Screen(route = "Notification", R.drawable.bell_icon)
    object Language : Screen(route = "Language", R.drawable.language_icon)
    object HelpAndSupport : Screen(route = "Help And Support", R.drawable.help_icon)
    object ManageBillsAndInstalment : Screen(route = "Bills & Instalment", R.drawable.bills_icon)
    object AddBills : Screen(route = "Add Bills", R.drawable.add_icon)
    object TaxCalculator : Screen(route = "Tax Calculator", R.drawable.calculator_icon)
    object AddExpenses : Screen(route = "Expenses", R.drawable.calculator_icon)
    object AddIncomes : Screen(route = "Incomes", R.drawable.calculator_icon)

    object Cash : Screen(route = "Cash", 0)
    object CashDetails : Screen(route = "CashDetails", 0)
    object AddStock : Screen (route = "AddStock", 0)
    object EditStock : Screen (route = "EditStock", 0)
    object AddExistingStock : Screen (route = "EditStock", 0)
    object FixedDepositDetails : Screen (route = "FixedDepositDetails", 0)

    object BudgetScreen : Screen(route = "Budget & Goal", R.drawable.cross_icon)


}