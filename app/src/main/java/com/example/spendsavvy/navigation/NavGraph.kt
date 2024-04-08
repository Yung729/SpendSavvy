package com.example.spendsavvy.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
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
import com.example.spendsavvy.component.HeaderTopBar
import com.example.spendsavvy.screen.AnalysisScreen
import com.example.spendsavvy.screen.ChangePassword
import com.example.spendsavvy.screen.categoryScreen.CategoryScreen
import com.example.spendsavvy.screen.ChangeProfileScreen
import com.example.spendsavvy.screen.LoginScreen
import com.example.spendsavvy.screen.MyProfileScreen
import com.example.spendsavvy.screen.OverviewScreen
import com.example.spendsavvy.screen.SettingsScreen
import com.example.spendsavvy.screen.SignUpScreen
import com.example.spendsavvy.screen.StockScreen
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

        composable(route = Screen.MainScreen.route) {
            TabsNavGraph()
        }

    }
}

@Composable
fun TabsNavGraph() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreenName = backStackEntry?.destination?.route ?: Screen.Overview.route

    Scaffold(
        topBar = {

            HeaderTopBar(
                text = currentScreenName,
                canNavBack = navController.previousBackStackEntry != null && currentScreenName !in listOf(
                    Screen.Overview.route,
                    Screen.Wallet.route,
                    Screen.Analysis.route,
                    Screen.Settings.route
                ),
                navUp = { navController.navigateUp() }
            )

        }, bottomBar = {

            NavigationBar(modifier = Modifier.height(70.dp)) {

                items.forEach { screen ->
                    NavigationBarItem(
                        selected = backStackEntry?.destination?.hierarchy?.any {
                            it.route == screen.route
                        } == true,
                        icon = {
                            Icon(
                                painter = painterResource(id = screen.iconResourceId),
                                contentDescription = null,
                                Modifier.size(20.dp, 20.dp)
                            )
                        },
                        onClick = {
                            navController.navigate(screen.route) {

                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                    inclusive = true
                                }

                                launchSingleTop = true
                                restoreState = true

                            }
                        }
                    )
                }
            }

        },
        floatingActionButton = {

            if (currentScreenName == Screen.Overview.route) {
                FloatingActionButton(
                    onClick = {

                    },
                    modifier = Modifier
                        .size(55.dp)
                        .offset(y = 50.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add"
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center

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
                        .padding(20.dp)
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
                        .padding(20.dp)
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

            composable(
                route = Screen.Stock.route
            ) {
                StockScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                )
            }

            composable(
                route = Screen.Category.route
            ) {
                CategoryScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                )
            }

            composable(
                route = Screen.MyProfile.route
            ) {
                MyProfileScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    navController = navController
                )
            }

            composable(
                route = Screen.ChangeProfile.route
            ) {
                ChangeProfileScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    navController = navController
                )
            }

            composable(
                route = Screen.ChangePassword.route
            ) {
                ChangePassword(
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


