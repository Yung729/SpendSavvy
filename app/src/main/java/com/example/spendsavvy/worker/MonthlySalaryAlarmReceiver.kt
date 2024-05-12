package com.example.spendsavvy.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.repo.FirestoreRepository
import com.example.spendsavvy.viewModels.DateSelectionViewModel
import com.example.spendsavvy.viewModels.OverviewViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Date

class MonthlySalaryAlarmReceiver : BroadcastReceiver() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context?, intent: Intent?) {

        val firestoreRepo = FirestoreRepository()

        GlobalScope.launch {

            val totalSalariesPerUser = firestoreRepo.totalStaffSalariesPerUser()

            // Iterate over the map and add total salary as an expense for each user
            totalSalariesPerUser.forEach { (userId, totalSalary) ->
                val transactionViewModel = context?.let {
                    OverviewViewModel(
                        context = it,
                        true,
                        userId,
                        DateSelectionViewModel()
                    )
                }

                if (totalSalary != 0.0) {


                    // Add the expense to Firestore
                    transactionViewModel?.addTransactionToAllUser(
                        userId = userId,
                        Transactions(
                            id = transactionViewModel.generateTransactionId(),
                            amount = totalSalary,
                            description = "Total Salary",
                            date = Date(),
                            category = Category(
                                id = "CT0013",
                                imageUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/spendsavvy-5a2a8.appspot.com/o/images%2Fsalary.png?alt=media&token=102737bc-9da6-48ef-827c-b0b05d9fb052"),
                                categoryName = "Staff Salary",
                                categoryType = "Expenses"
                            ),
                            paymentMethod = "Cash",
                            transactionType = "Expenses"
                        ),
                        onSuccess = {
                            transactionViewModel.getTransactionRecord()
                        },
                        onFailure = {}
                    )
                }
            }
        }

    }
}
