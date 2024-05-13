package com.example.spendsavvy.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.spendsavvy.R
import android.Manifest
import androidx.core.app.ActivityCompat
import com.example.spendsavvy.repo.FirestoreRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BillsAlarmReceiver : BroadcastReceiver() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context?, intent: Intent?) {
        val firestoreRepo = FirestoreRepository()

        GlobalScope.launch {
            val remainingDaysPerBill = firestoreRepo.calculateBillsRemainingDate()

            remainingDaysPerBill.forEach { (billId, billInfo) ->
                val selectedDueDate = billInfo.selectedDueDate
                val currentDate = Date()

                val differenceInMillis = selectedDueDate.time - currentDate.time
                val daysLeft = if (differenceInMillis == 0L) 0L else billInfo.remainingDays

                val description = billInfo.description
                val amount = billInfo.amount
                val dueDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(billInfo.selectedDueDate)

                val notificationMessage = when (daysLeft) {
                    0L -> "Bill $billId ($description) with amount $amount is due today!"
                    1L -> "Bill $billId ($description) with amount $amount is due tomorrow!"
                    else -> "Bill $billId ($description) with amount $amount is due in $daysLeft days (Due Date: $dueDate)."
                }

                sendNotification(context, notificationMessage)
            }

        }
    }
    private fun sendNotification(context: Context?, notificationMessage: String) {
        if (context != null) {
            createBillNotification(context, notificationMessage)
        }
    }

    private fun createBillNotification(context: Context,message: String){
        val notificationId = 1
        val builder = NotificationCompat.Builder(context, "CHANNEL_ID")
            .setSmallIcon(R.drawable.bills_icon)
            .setContentTitle("New bill made")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(context)){

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(notificationId, builder.build())
        }
    }
}