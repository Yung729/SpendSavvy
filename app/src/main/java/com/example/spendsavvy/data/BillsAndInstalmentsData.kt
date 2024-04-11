package com.example.spendsavvy.data

//import com.example.spendsavvy.models.BillsAndInstalments
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.Locale
//
//class BillsAndInstalmentsData {
//
//    private fun formatDate(date: Date): String {
//        val dataFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
//        return dataFormat.format(date)
//    }
//
//    fun loadSelectedBills(): List<BillsAndInstalments> {
//        val categories = CategoryData().loadCategory()
//        val today = formatDate(Date())
//
//
//        val upcomingBills = mutableListOf<BillsAndInstalments>()
//
//        val billsRecord = listOf<BillsAndInstalments>(
//            BillsAndInstalments("Utilities", "Electricity Bill", 100.0, formatDate(Date(2023, 3, 20)), "Upcoming"),
//            BillsAndInstalments("Utilities", "Water Bill", 50.0, formatDate(Date(2023, 3, 15)), "paid"),
//            BillsAndInstalments("Rent", "Monthly Rent", 1200.0, formatDate(Date(2024, 4, 1)), "Upcoming"),
//            BillsAndInstalments("Utilities", "Car Instalment", 80.0, formatDate(Date(2023, 3, 20)), "Overdue"),
//        )
//
//        for (bills in billsRecord) {
//            if (bills.date == today) {
//                upcomingBills.add(bills)
//            }
//        }
//
//        return upcomingBills
//
//
//    }
//}