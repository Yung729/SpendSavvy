package com.example.spendsavvy.data

import com.example.spendsavvy.R
import com.example.spendsavvy.model.Transactions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class TransactionData {
    private fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        return sdf.format(date)
    }
    fun loadTransactions(): List<Transactions> {
        val categories = CategoryData().loadCategory()

        return listOf<Transactions>(
           Transactions(R.drawable.wallet,20.00,categories[0],"", formatDate(Date())),
           Transactions(R.drawable.wallet,20.00,categories[1],"", formatDate(Date())),
           Transactions(R.drawable.wallet,20.00,categories[2],"", formatDate(Date())),
           Transactions(R.drawable.wallet,20.00,categories[3],"", formatDate(Date())),
           Transactions(R.drawable.wallet,20.00,categories[4],"", formatDate(Date())),
           Transactions(R.drawable.wallet,20.00,categories[5],"", formatDate(Date())),
           Transactions(R.drawable.wallet,20.00,categories[7],"", formatDate(Date()))
        )

    }
}