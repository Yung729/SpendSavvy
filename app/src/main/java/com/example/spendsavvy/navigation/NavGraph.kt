package com.example.spendsavvy.navigation

import android.net.Uri
import android.os.Parcelable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.spendsavvy.components.HeaderTopBar
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.screen.AddCategoryScreen
import com.example.spendsavvy.screen.AddExpensesScreen
import com.example.spendsavvy.screen.AddIncomeScreen
import com.example.spendsavvy.screen.AnalysisScreen
import com.example.spendsavvy.screen.CategoryDetail
import com.example.spendsavvy.screen.CategoryScreen
import com.example.spendsavvy.screen.ChangePassword
import com.example.spendsavvy.screen.ChangeProfileScreen
import com.example.spendsavvy.screen.CreatePassword
import com.example.spendsavvy.screen.ForgotPassword
import com.example.spendsavvy.screen.HelpAndSupport
import com.example.spendsavvy.screen.Language
import com.example.spendsavvy.screen.LoginScreen
import com.example.spendsavvy.screen.ManageBillsAndInstalment
import com.example.spendsavvy.screen.MyProfileScreen
import com.example.spendsavvy.screen.Notification
import com.example.spendsavvy.screen.OverviewScreen
import com.example.spendsavvy.screen.SettingsScreen
import com.example.spendsavvy.screen.SignUpScreen
import com.example.spendsavvy.screen.StockScreen
import com.example.spendsavvy.screen.TaxCalculator
import com.example.spendsavvy.screen.WalletScreen
import com.example.spendsavvy.ui.theme.ButtonColor
import com.example.spendsavvy.viewModels.CategoryViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController(), auth: FirebaseAuth) {

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
fun TabsNavGraph(navController: NavHostController =  rememberNavController()) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreenName = backStackEntry?.destination?.route ?: Screen.Overview.route

    val showOption = remember { mutableStateOf(false) }
    val categoryViewModel : CategoryViewModel = viewModel()

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
                                restoreState = true
                                launchSingleTop = false

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
                        showOption.value = !showOption.value
                    },
                    elevation = FloatingActionButtonDefaults.elevation(8.dp),
                    containerColor = ButtonColor,
                    contentColor = Color.White,
                    modifier = Modifier
                        .offset(y = 50.dp, x = if (showOption.value) 21.dp else 0.dp)
                        .size(50.dp)

                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add"
                    )
                }

                if (showOption.value) {

                    FloatingActionButton(
                        onClick = {
                            navController.navigate(Screen.AddExpenses.route)
                        },
                        modifier = Modifier
                            .size(95.dp, 50.dp)
                            .offset(x = (-65).dp, y = (-25).dp), // Position to the left of the main FAB
                        elevation = FloatingActionButtonDefaults.elevation(8.dp),
                        containerColor = ButtonColor,
                        contentColor = Color.White
                    ) {
                        Text(text = "Expense")
                    }

                    FloatingActionButton(
                        onClick = {
                            navController.navigate(Screen.AddIncomes.route)
                        },
                        modifier = Modifier
                            .size(95.dp, 50.dp)
                            .offset(x = 65.dp, y = (-25).dp), // Position to the right of the main FAB
                        elevation = FloatingActionButtonDefaults.elevation(8.dp),
                        containerColor = ButtonColor,
                        contentColor = Color.White
                    ) {
                        Text(text = "Income")
                    }
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
                DisposableEffect(Unit) {

                    onDispose { showOption.value = false }
                }

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
                        .padding(20.dp),
                    catViewModel = categoryViewModel,
                    navController = navController
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

            composable(
                route = Screen.ForgotPassword.route
            ) {
                ForgotPassword(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    navController = navController
                )
            }
            composable(
                route = Screen.CreatePassword.route
            ) {
                CreatePassword(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    navController = navController
                )
            }
            composable(
                route = Screen.ManageBillsAndInstalment.route
            ) {
                ManageBillsAndInstalment(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    navController = navController
                )
            }
            composable(
                route = Screen.TaxCalculator.route
            ) {
                TaxCalculator(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    navController = navController
                )
            }


            composable(
                route = Screen.Notifications.route
            ) {
                Notification(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    navController = navController
                )
            }

            composable(
                route = Screen.Language.route
            ) {
                Language(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    navController = navController
                )
            }

            composable(
                route = Screen.HelpAndSupport.route
            ) {
                HelpAndSupport(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    navController = navController
                )
            }
            composable(
                route = Screen.AddExpenses.route
            ) {
                AddExpensesScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    navController = navController
                )
            }

            composable(
                route = Screen.AddIncomes.route
            ) {
                AddIncomeScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    navController = navController
                )
            }


            composable(
                route = Screen.AddCategory.route
            ) {

                AddCategoryScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    catViewModel = categoryViewModel
                )
            }

            composable(
                route = Screen.CategoryDetail.route ,

            ) {backStackEntry->


                CategoryDetail(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    catViewModel = categoryViewModel,
                    category = Category()
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


