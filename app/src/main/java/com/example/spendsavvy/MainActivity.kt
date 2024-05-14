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
import com.example.spendsavvy.ui.theme.rememberWindowSize
import com.example.spendsavvy.worker.BillsAlarmReceiver
import java.util.Calendar


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            SpendSavvyTheme {
                val context = LocalContext.current
                val window = rememberWindowSize()
                SetupNavGraph(context = context, window = window)

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
