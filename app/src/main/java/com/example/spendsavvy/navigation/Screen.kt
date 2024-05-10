package com.example.spendsavvy.navigation

import com.example.spendsavvy.R

sealed class Screen(val route: String, val iconResourceId: Int) {

    object Login : Screen(route = "login_screen", 0)
    object SignUp : Screen(route = "signUp_screen", 0)
    object Overview : Screen(route = "Overview", R.drawable.bar_chart)
    object AllTransaction : Screen(route = "Transaction Record", 0)
    object TransactionDetails : Screen(route = "Transaction Details", 0)
    object Wallet : Screen(route = "Wallet", R.drawable.wallet)

    object Settings : Screen(route = "Settings", R.drawable.settings_icon)
    object Analysis : Screen(route = "Analysis", R.drawable.analysis_icon)
    object Stock : Screen(route = "Stock", 0)
    object Category : Screen(route = "Category", 0)
    object AddCategory : Screen(route = "Add Category", 0)
    object CategoryDetail : Screen(route = "Category Details", 0)
    object MainScreen : Screen(route = "EnterMainScreen", 0)

    object ChangeProfile : Screen(route = "Edit Profile", 0)
    object MyProfile : Screen(route = "My Profile", 0)

    object ChangePassword : Screen(route = "Change Password ", 0)
    object ForgotPassword : Screen(route = "Forgot Password", 0)
    object CreatePassword : Screen(route = "Create Password", 0)
    object Notifications : Screen(route = "Notification", 0)
    object Language : Screen(route = "Language", 0)
    object HelpAndSupport : Screen(route = "Help And Support", 0)
    object ManageBillsAndInstalment : Screen(route = "Bills & Instalment", 0)
    object AddBills : Screen(route = "Add Bills", 0)
    object EditBills : Screen(route = "Edit Bills", 0)
    object TaxCalculator : Screen(route = "Tax Calculator", 0)
    object AddExpenses : Screen(route = "Expenses", 0)
    object AddIncomes : Screen(route = "Incomes", 0)

    object Cash : Screen(route = "Cash", 0)
    object CashDetails : Screen(route = "CashDetails", 0)
    object AddStock : Screen (route = "AddStock", 0)
    object EditStock : Screen (route = "EditStock", 0)
    object AddExistingStock : Screen (route = "EditStock", 0)
    object FixedDepositDetails : Screen (route = "FixedDepositDetails", 0)

    object BudgetScreen : Screen(route = "Budget & Goal", 0)

    object StaffScreen : Screen(route = "Staff",0)
    object StaffDetailScreen : Screen(route = "Edit Staff",0)
    object AddStaffScreen : Screen(route = "Add Staff",0)


}