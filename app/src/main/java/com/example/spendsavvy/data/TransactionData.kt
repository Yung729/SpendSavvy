package com.example.spendsavvy.data

import com.example.spendsavvy.model.Transactions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class TransactionData {
    private fun formatDate(date: Date): String {
        val dataFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        return dataFormat.format(date)
    }

    fun loadTodayTransactions(): List<Transactions> {
        val categories = CategoryData().loadCategory()
        val today = formatDate(Date())


        val todayTransactions = mutableListOf<Transactions>()

        val transactionsRecord = listOf<Transactions>(
            Transactions(20.00, categories[0], "", formatDate(Date(2023, 3, 25, 5, 20, 50))),
            Transactions(20.00, categories[1], "", formatDate(Date(2023, 3, 25, 5, 20, 50))),
            Transactions(20.00, categories[2], "", formatDate(Date(2023, 3, 25, 5, 20, 50))),
            Transactions(20.00, categories[3], "", formatDate(Date(2023, 3, 25, 5, 20, 50))),
            Transactions(20.00, categories[4], "", formatDate(Date(2023, 3, 25, 5, 20, 50))),
            Transactions(20.00, categories[5], "", formatDate(Date(2023, 3, 25, 5, 20, 50))),
            Transactions(20.00, categories[6], "", formatDate(Date(2023, 3, 25, 5, 20, 50))),
            Transactions(20.00, categories[7], "", formatDate(Date(2023, 3, 25, 5, 20, 50))),
            Transactions(20.00, categories[0], "", today),
            Transactions(20.00, categories[2], "", today),
            Transactions(20.00, categories[7], "", today),
            Transactions(20.00, categories[7], "", today),
            Transactions(20.00, categories[7], "", today),
            Transactions(20.00, categories[7], "", today),
            Transactions(20.00, categories[7], "", today)
        )

        for (transaction in transactionsRecord) {
            if (transaction.date == today) {
                todayTransactions.add(transaction)
            }
        }

        return todayTransactions


    }
}