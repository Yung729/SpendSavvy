package com.example.spendsavvy.worker

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.repo.FirestoreRepository
import com.example.spendsavvy.viewModels.DateSelectionViewModel
import com.example.spendsavvy.viewModels.OverviewViewModel
import com.example.spendsavvy.viewModels.StaffViewModel
import java.util.Date

class MonthlySalaryWorker(
    context: Context,
    params: WorkerParameters
) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        return try {
            val firestoreRepo = FirestoreRepository()

            // Retrieve total staff salaries per user
            val totalSalariesPerUser = firestoreRepo.totalStaffSalariesPerUser()

            // Iterate over the map and add total salary as an expense for each user
            totalSalariesPerUser.forEach { (userId, totalSalary) ->
                val transactionViewModel = OverviewViewModel(context = applicationContext, true, userId, DateSelectionViewModel())

                if (totalSalary != 0.0) {
                    // Add the expense to Firestore
                    transactionViewModel.addTransactionToAllUser(
                        userId = userId,
                        Transactions(
                            id = transactionViewModel.generateTransactionId(),
                            amount = totalSalary,
                            description = "Total Salary",
                            date = Date(),
                            category = Category(
                                id = "T0013",
                                imageUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/spendsavvy-5a2a8.appspot.com/o/images%2Fsalary.png?alt=media&token=102737bc-9da6-48ef-827c-b0b05d9fb052"),
                                categoryName = "Total Staff Salary",
                                categoryType = "Expenses"
                            ),
                            transactionType = "Expenses"
                        ),
                        onSuccess = {
                            transactionViewModel.getTransactionRecord()
                        },
                        onFailure = {}
                    )
                }
            }

            Result.success()

        } catch (e: Exception) {
            Result.failure()
        }
    }
}