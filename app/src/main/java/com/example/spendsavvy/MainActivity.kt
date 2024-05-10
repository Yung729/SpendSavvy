package com.example.spendsavvy

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import com.example.spendsavvy.navigation.SetupNavGraph
import com.example.spendsavvy.ui.theme.SpendSavvyTheme
import com.example.spendsavvy.worker.MonthlySalaryAlarmReceiver
import java.util.Calendar


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        scheduleMonthlySalaryAlarm(applicationContext)
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

@SuppressLint("ScheduleExactAlarm")
fun scheduleMonthlySalaryAlarm(context: Context) {
    // Create an Intent that will be triggered by the alarm
    val intent = Intent(context, MonthlySalaryAlarmReceiver::class.java)
    // Create a PendingIntent that will be triggered by the alarm
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_IMMUTABLE
    )

    // Get the AlarmManager service
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // Set the end of the current month
    val endOfMonth = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, 1) // Start with the first day of the next month
        set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH) + 1) // Go to the next month
        add(Calendar.DATE, -1) // Subtract one day to get the last day of the current month
        set(Calendar.HOUR_OF_DAY, 0) // Set the hour to midnight
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    // Schedule the alarm for the end of the month
    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        endOfMonth.timeInMillis,
        pendingIntent
    )
}