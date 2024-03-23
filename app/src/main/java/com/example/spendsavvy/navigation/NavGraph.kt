package com.example.spendsavvy.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.R
import com.example.spendsavvy.screen.AnalysisScreen
import com.example.spendsavvy.screen.LoginScreen
import com.example.spendsavvy.screen.OverviewScreen
import com.example.spendsavvy.screen.SettingsScreen
import com.example.spendsavvy.screen.SignUpScreen
import com.example.spendsavvy.screen.WalletScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SetupNavGraph(navController: NavHostController, auth: FirebaseAuth) {

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
                navController = navController,
                auth = auth

            )
        }

        composable(
            route = Screen.SignUp.route
        ) {
            SignUpScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                navController = navController,
                auth = auth
            )
        }

        composable(route = "EnterMainScreen") {
            TabsNavGraph()
        }
    }
}

@Composable
fun TabsNavGraph() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar(modifier = Modifier.height(70.dp)) {
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = backStackEntry?.destination

                items.forEach { screen ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any {
                            it.route == screen.route
                        } == true,
                        icon = {
                            Icon(
                                painter =
                                when (screen.route) {
                                    "overview_screen" -> painterResource(id = R.drawable.bar_chart)
                                    "wallet_screen" -> painterResource(id = R.drawable.wallet)
                                    "analysis_screen" -> painterResource(id = R.drawable.analysis_icon)
                                    else -> painterResource(id = R.drawable.settings_icon)
                                },
                                contentDescription = null,
                                Modifier.size(20.dp, 20.dp)
                            )
                        },
                        onClick = {
                            navController.navigate(screen.route) {

                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }

                                launchSingleTop = true

                                restoreState = true

                            }
                        },
                    )
                }
            }

        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Overview.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(route = Screen.Overview.route) {
                OverviewScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    navController = navController
                )
            }

            composable(route = Screen.Wallet.route) {
                WalletScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    navController = navController
                )
            }

            composable(route = Screen.Analysis.route) {
                AnalysisScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    navController = navController
                )
            }

            composable(route = Screen.Settings.route) {
                SettingsScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    navController = navController
                )
            }


        }

    }
}

val items = listOf(
    Screen.Overview,
    Screen.Wallet,
    Screen.Analysis,
    Screen.Settings
)


