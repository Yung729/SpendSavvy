package com.example.spendsavvy.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.spendsavvy.viewModels.OverviewViewModel
import com.example.spendsavvy.viewModels.StaffViewModel

class AppWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    val overviewViewModel: OverviewViewModel,
    val userId: String
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        try {
            val staffViewModel = StaffViewModel(
                applicationContext,
                true,
                userId = userId,
                transactionViewModel = overviewViewModel
            )
            staffViewModel.addTotalStaffSalaryToExpenses()
            return Result.success()
        } catch (e: Exception) {
            Log.e("AddStaffSalaryWorker", "Error adding total staff salary to expenses", e)
            return Result.failure()
        }
    }


}