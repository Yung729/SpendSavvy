package com.example.spendsavvy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.navigation.SetupNavGraph
import com.example.spendsavvy.ui.theme.SpendSavvyTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpendSavvyTheme {

                navController = rememberNavController()
                auth = FirebaseAuth.getInstance()
                SetupNavGraph(navController = navController,auth)

            }
        }
    }
}
