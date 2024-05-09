package com.example.spendsavvy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import com.example.spendsavvy.navigation.SetupNavGraph
import com.example.spendsavvy.ui.theme.SpendSavvyTheme


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

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
