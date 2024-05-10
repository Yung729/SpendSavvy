package com.example.spendsavvy

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.spendsavvy.navigation.SetupNavGraph
import com.example.spendsavvy.ui.theme.SpendSavvyTheme
import com.example.spendsavvy.worker.MonthlySalaryWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        scheduleMonthlySalaryWorker(applicationContext)
        setContent {
            SpendSavvyTheme {
                val context = LocalContext.current
                SetupNavGraph(context = context)

            }
        }
    }

    public override fun onStart() {
        super.onStart()
    }

    public override fun onStop() {
        super.onStop()
    }


}

fun scheduleMonthlySalaryWorker(
    context: Context
) {

    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val today = Calendar.getInstance()
    val currentMonth = today.get(Calendar.MONTH)

    // Set the end of the current month
    val endOfMonth = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, 1) // Start with the first day of the next month
        set(Calendar.MONTH, currentMonth + 1) // Go to the next month
        add(Calendar.DATE, -1) // Subtract one day to get the last day of the current month
    }

    // Calculate the time until the end of the month
    val timeUntilEndOfMonth = endOfMonth.timeInMillis - today.timeInMillis


    val request = PeriodicWorkRequest.Builder(
        MonthlySalaryWorker::class.java,
        28,
        TimeUnit.DAYS
    )
        .setConstraints(constraints)
        .setInitialDelay(timeUntilEndOfMonth, TimeUnit.MILLISECONDS)
        .build()



    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "MonthlySalaryWorker", // Unique name for this work request
        ExistingPeriodicWorkPolicy.KEEP, // Keep existing work if it exists
        request // The work request to be enqueued
    )
}
