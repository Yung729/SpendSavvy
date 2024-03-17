package com.example.spendsavvy

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun SetupNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    )
    {
        composable(
            route = Screen.Login.route
        ) {
            LoginScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                navController = navController
            )
        }

        composable(
            route = Screen.SignUp.route
        ) {
            SignUpScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                navController = navController
            )
        }
    }
}