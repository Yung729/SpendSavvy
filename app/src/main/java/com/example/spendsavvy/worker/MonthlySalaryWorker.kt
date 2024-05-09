package com.example.spendsavvy.worker

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.Transactions
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
            //staffViewModel.addTotalStaffSalaryToExpenses()
            val isOnline = inputData.getBoolean("isOnline", false)
            val userId = inputData.getString("userId") as String
            val totalStaffSalary = inputData.getDouble("totalStaffSalary", 0.0)

            val transactionViewModel = OverviewViewModel(context = applicationContext,isOnline,userId,DateSelectionViewModel())


            // Add the expense to Firestore or wherever you store your expenses
            transactionViewModel.addTransactionToFirestore(
                Transactions(
                    id = transactionViewModel.generateTransactionId(),
                    amount = totalStaffSalary,
                    description = "Salary",
                    date = Date(),
                    category = Category(
                        id = "T0013",
                        imageUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/spendsavvy-5a2a8.appspot.com/o/images%2Fsalary.png?alt=media&token=102737bc-9da6-48ef-827c-b0b05d9fb052"),
                        categoryName = "Staff Salary",
                        categoryType = "Expenses"
                    ),
                    transactionType = "Expenses"
                ),
                onSuccess = {
                    Toast.makeText(
                        applicationContext,
                        "Total staff salary added to expenses successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onFailure = {
                    Toast.makeText(
                        applicationContext,
                        "Failed to add total staff salary to expenses",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )

            Result.success()

        } catch (e: Exception) {
            Result.failure()
        }
    }
}