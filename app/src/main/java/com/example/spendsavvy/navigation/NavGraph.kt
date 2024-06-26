package com.example.spendsavvy.navigation

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.spendsavvy.R
import com.example.spendsavvy.components.HeaderTopBar
import com.example.spendsavvy.models.Bills
import com.example.spendsavvy.models.Cash
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.FDAccount
import com.example.spendsavvy.models.Staff
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.repo.FireAuthRepository
import com.example.spendsavvy.screen.AddBills
import com.example.spendsavvy.screen.AddCashAccountScreen
import com.example.spendsavvy.screen.AddCategoryScreen
import com.example.spendsavvy.screen.AddExpensesScreen
import com.example.spendsavvy.screen.AddIncomeScreen
import com.example.spendsavvy.screen.AddNewStockScreen
import com.example.spendsavvy.screen.AllTransactionScreen
import com.example.spendsavvy.screen.AnalysisScreen
import com.example.spendsavvy.screen.BudgetScreen
import com.example.spendsavvy.screen.CashScreen
import com.example.spendsavvy.screen.CashTransactionScreen
import com.example.spendsavvy.screen.CategoryDetail
import com.example.spendsavvy.screen.CategoryScreen
import com.example.spendsavvy.screen.ChangePassword
import com.example.spendsavvy.screen.ChangeProfileScreen
import com.example.spendsavvy.screen.CreatePassword
import com.example.spendsavvy.screen.EditBills
import com.example.spendsavvy.screen.EditCashAccountScreen
import com.example.spendsavvy.screen.EditExistingStockScreen
import com.example.spendsavvy.screen.FDEarnScreen
import com.example.spendsavvy.screen.FixedDepositDetailsScreen
import com.example.spendsavvy.screen.FixedDepositScreen
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
import com.example.spendsavvy.screen.StaffAddScreen
import com.example.spendsavvy.screen.StaffDetailScreen
import com.example.spendsavvy.screen.StaffScreen
import com.example.spendsavvy.screen.StockScreen
import com.example.spendsavvy.screen.TaxCalculator
import com.example.spendsavvy.screen.TransactionDetail
import com.example.spendsavvy.screen.WalletScreen
import com.example.spendsavvy.ui.theme.ButtonColor
import com.example.spendsavvy.ui.theme.ScreenSize
import com.example.spendsavvy.viewModels.BillsViewModel
import com.example.spendsavvy.viewModels.CategoryViewModel
import com.example.spendsavvy.viewModels.DateSelectionViewModel
import com.example.spendsavvy.viewModels.NotificationViewModel
import com.example.spendsavvy.viewModels.OverviewViewModel
import com.example.spendsavvy.viewModels.ProfileViewModel
import com.example.spendsavvy.viewModels.QuestionViewModel
import com.example.spendsavvy.viewModels.StaffViewModel
import com.example.spendsavvy.viewModels.TargetViewModel
import com.example.spendsavvy.viewModels.WalletViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SetupNavGraph(
    navController: NavHostController = rememberNavController(),
    context: Context,
    window: ScreenSize
) {


    val connection by connectivityStatus()

    val isConnected = connection === ConnectionStatus.Available
    var showSnackBar by remember {
        mutableStateOf(!isConnected)
    }

    LaunchedEffect(isConnected) {
        if (!isConnected) {
            showSnackBar = true
        }
    }


    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreenName = backStackEntry?.destination?.route ?: Screen.Overview.route

    val fireAuthRepository = FireAuthRepository(
        context = context,
        CategoryViewModel(context, isConnected, "adminUser"),
        isConnected
    )

    val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("UserAcc", Context.MODE_PRIVATE)
    }
    var userId by remember { mutableStateOf("adminUser") }
    userId = fireAuthRepository.getCurrentUserId()


    if (isConnected) {
        if (userId == "") {
            userId = "adminUser"
        }
    } else {
        userId = sharedPreferences.getString("userID", "")!!
    }


    val walletViewModel = WalletViewModel(context, isConnected, userId)
    val dateViewModel = DateSelectionViewModel()
    val categoryViewModel = CategoryViewModel(context, isConnected, userId)
    val transactionsViewModel =
        OverviewViewModel(context, isConnected, userId, dateViewModel, walletViewModel)
    val targetViewModel = TargetViewModel(context, isConnected, userId)
    val staffViewModel = StaffViewModel(context, isConnected, userId, transactionsViewModel)
    val profileViewModel = ProfileViewModel(userId, isConnected, context)
    val notificationViewModel = NotificationViewModel(context)
    val billsViewModel =
        BillsViewModel(context, isConnected, userId, notificationViewModel, transactionsViewModel)
    val questionsViewModel = QuestionViewModel(context, isConnected, userId)
    val showOption = remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            if (currentScreenName !in listOf(
                    Screen.Login.route,
                    Screen.SignUp.route,
                )
            ) {

                HeaderTopBar(text = currentScreenName,
                    canNavBack = navController.previousBackStackEntry != null && currentScreenName !in listOf(
                        Screen.Overview.route,
                        Screen.Wallet.route,
                        Screen.Analysis.route,
                        Screen.Settings.route
                    ),
                    navUp = { navController.navigateUp() })
            }

        },
        bottomBar = {
            if (currentScreenName !in listOf(
                    Screen.Login.route,
                    Screen.SignUp.route,
                    Screen.ForgotPassword.route
                )
            ) {

                NavigationBar(
                    modifier = Modifier.height(70.dp),
                    containerColor = Color(0xFFfdfdfd),
                    tonalElevation = 5.dp
                ) {

                    items.forEach { screen ->
                        NavigationBarItem(
                            selected = backStackEntry?.destination?.hierarchy?.any {
                                it.route == screen.route
                            } == true,
                            icon = {
                                Icon(
                                    painter = painterResource(id = screen.iconResourceId),
                                    contentDescription = null,
                                    Modifier.size(16.dp, 16.dp)
                                )

                            }, label = {
                                Text(
                                    text = screen.route,
                                    fontSize = 11.sp
                                )
                            },
                            colors = NavigationBarItemColors(
                                selectedIconColor = Color(0xFF6347EB),
                                selectedTextColor = Color(0xFF6347EB),
                                selectedIndicatorColor = Color.Transparent,
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray,
                                disabledIconColor = Color.Gray,
                                disabledTextColor = Color.Gray
                            ),
                            onClick = {
                                navController.navigate(screen.route) {

                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                        inclusive = true
                                    }
                                    restoreState = true
                                    launchSingleTop = false

                                }
                            })
                    }
                }
            }
        },
        floatingActionButton = {
            if (currentScreenName !in listOf(
                    Screen.Login.route,
                    Screen.SignUp.route,
                    Screen.ForgotPassword.route
                )
            ) {

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
                            Icons.Default.Add, contentDescription = "Add"
                        )
                    }

                    if (showOption.value) {

                        FloatingActionButton(
                            onClick = {
                                navController.navigate(Screen.AddExpenses.route)
                            },
                            modifier = Modifier
                                .size(95.dp, 50.dp)
                                .offset(
                                    x = (-65).dp, y = (-25).dp
                                ), // Position to the left of the main FAB
                            elevation = FloatingActionButtonDefaults.elevation(8.dp),
                            containerColor = ButtonColor,
                            contentColor = Color.White
                        ) {
                            Text(text = stringResource(id = R.string.expense))
                        }

                        FloatingActionButton(
                            onClick = {
                                navController.navigate(Screen.AddIncomes.route)
                            },
                            modifier = Modifier
                                .size(95.dp, 50.dp)
                                .offset(
                                    x = 65.dp, y = (-25).dp
                                ), // Position to the right of the main FAB
                            elevation = FloatingActionButtonDefaults.elevation(8.dp),
                            containerColor = ButtonColor,
                            contentColor = Color.White
                        ) {
                            Text(text = stringResource(id = R.string.income))
                        }
                    }

                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        snackbarHost = {


            if (!isConnected && showSnackBar) {

                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    action = {
                        TextButton(onClick = { showSnackBar = false }) {
                            Text("Close", color = Color.White)
                        }
                    }
                ) {
                    Text(text = "No Internet Connection")
                }

            } else if (isConnected && !showSnackBar) {
                showSnackBar = false
            }

        }
    ) { innerPadding ->


        NavHost(
            navController = navController,
            startDestination = "First",
            modifier = Modifier.padding(innerPadding)
        ) {


            navigation(startDestination = Screen.Login.route, route = "First") {

                composable(
                    route = Screen.Login.route
                ) {
                    LoginScreen(
                        context = context,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        navController = navController,
                        fireAuthRepository = fireAuthRepository
                    )
                    userId = fireAuthRepository.getCurrentUserId()
                }

                composable(
                    route = Screen.SignUp.route
                ) {
                    SignUpScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        navController = navController,
                        fireAuthRepository = fireAuthRepository
                    )
                }

                composable(
                    route = Screen.ForgotPassword.route
                ) {
                    ForgotPassword(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        navController = navController,
                        profileViewModel = profileViewModel,
                        fireAuthRepository = fireAuthRepository
                    )
                }

            }

            navigation(startDestination = Screen.Overview.route, route = "Second") {


                composable(route = Screen.Overview.route) {

                    DisposableEffect(Unit) {
                        onDispose { showOption.value = false }
                    }


                    OverviewScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        transactionViewModel = transactionsViewModel,
                        budgetViewModel = targetViewModel,
                        dateViewModel = dateViewModel,
                        profileViewModel = profileViewModel,
                        walletViewModel = walletViewModel,
                        navController = navController,
                        window = window
                    )

                }

                composable(route = Screen.Wallet.route) {


                    WalletScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp), navController = navController,
                        walletViewModel = walletViewModel
                    )

                }

                composable(route = Screen.Analysis.route) {

                    AnalysisScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        navController = navController,
                        transactionViewModel = transactionsViewModel,
                        budgetViewModel = targetViewModel,
                        window = window
                    )

                }

                composable(route = Screen.Settings.route) {

                    SettingsScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        navController = navController,
                        profileViewModel = profileViewModel,
                        fireAuthRepository = fireAuthRepository
                    )

                }

                composable(
                    route = Screen.Cash.route
                ) {

                    CashScreen(
                        cashViewModel = walletViewModel,
                        navController = navController
                    )

                }

                composable(
                    route = Screen.AddCashAccount.route
                ) {

                    AddCashAccountScreen(
                        walletViewModel = walletViewModel,
                        transactionViewModel = transactionsViewModel,
                        navController = navController
                    )

                }

                composable(
                    route = Screen.EditCashAccount.route
                ) {

                    EditCashAccountScreen(
                        walletViewModel = walletViewModel,
                        transactionViewModel = transactionsViewModel,
                        navController = navController
                    )

                }

                composable(
                    route = Screen.Stock.route
                ) {

                    StockScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        walletViewModel = walletViewModel,
                        navController = navController
                    )

                }

                composable(
                    route = Screen.AddStock.route
                ) {

                    AddNewStockScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        walletViewModel = walletViewModel,
                        transactionViewModel = transactionsViewModel,
                        navController = navController
                    )

                }

                composable(
                    route = Screen.AddExistingStock.route
                ) {

                    EditExistingStockScreen(
                        walletViewModel = walletViewModel,
                        transactionViewModel = transactionsViewModel,
                        navController = navController,
                        mode = 1
                    )

                }

                composable(
                    route = Screen.EditStock.route
                ) {

                    EditExistingStockScreen(
                        walletViewModel = walletViewModel,
                        transactionViewModel = transactionsViewModel,
                        navController = navController,
                        mode = 2
                    )

                }

                composable(
                    route = Screen.FixedDepositScreen.route
                ) {

                    FixedDepositScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        walletViewModel = walletViewModel,
                        navController = navController
                    )

                }

                composable(
                    route = Screen.FixedDepositDetails.route
                ) {

                    FixedDepositDetailsScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        walletViewModel = walletViewModel,
                        navController = navController
                    )

                }

                composable(
                    route = Screen.FDEarnScreen.route
                ) {
                    val selectedFDAccount =
                        navController.previousBackStackEntry?.savedStateHandle?.get<FDAccount>("currentFDAccount")

                    if (selectedFDAccount != null) {
                        FDEarnScreen(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            navController = navController,
                            fdAccount = selectedFDAccount,
                            walletViewModel = walletViewModel
                        )

                    }
                }

                composable(
                    route = Screen.CashTransactionScreen.route
                ) {
                    val selectedCashAccount =
                        navController.previousBackStackEntry?.savedStateHandle?.get<Cash>("currentCashAccount")

                    if (selectedCashAccount != null) {
                        CashTransactionScreen(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            navController = navController,
                            cash = selectedCashAccount,
                            transactionViewModel = transactionsViewModel
                        )

                    }
                }

                composable(
                    route = Screen.Category.route
                ) {

                    CategoryScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        catViewModel = categoryViewModel,
                        navController = navController,
                        window = window
                    )

                }

                composable(
                    route = Screen.MyProfile.route
                ) {

                    MyProfileScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        navController = navController, profileViewModel = profileViewModel,
                    )

                }

                composable(
                    route = Screen.ChangeProfile.route
                ) {

                    ChangeProfileScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        navController = navController,
                        fireAuthRepository = fireAuthRepository,
                        profileViewModel = profileViewModel
                    )

                }

                composable(
                    route = Screen.ChangePassword.route
                ) {

                    ChangePassword(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp), navController = navController
                    )

                }


                composable(
                    route = Screen.CreatePassword.route
                ) {

                    CreatePassword(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp), navController = navController
                    )

                }
                composable(
                    route = Screen.ManageBillsAndInstalment.route
                ) {

                    ManageBillsAndInstalment(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        navController = navController,
                        billsViewModel = billsViewModel,
                        transactionViewModel = transactionsViewModel,
                        walletViewModel = walletViewModel
                    )

                }
                composable(
                    route = Screen.AddBills.route
                ) {

                    AddBills(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp), navController = navController,
                        billsViewModel = billsViewModel,
                        catViewModel = categoryViewModel
                    )

                }

                composable(
                    route = Screen.EditBills.route
                ) {
                    val selectedBills =
                        navController.previousBackStackEntry?.savedStateHandle?.get<Bills>("currentBill")

                    if (selectedBills != null) {

                        EditBills(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp), navController = navController,
                            bill = selectedBills,
                            billsViewModel = billsViewModel,
                            catViewModel = categoryViewModel
                        )

                    }
                }

                composable(
                    route = Screen.TaxCalculator.route
                ) {

                    TaxCalculator(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp), navController = navController,
                        transactionViewModel = transactionsViewModel,
                        walletViewModel = walletViewModel
                    )

                }


                composable(
                    route = Screen.Notifications.route
                ) {

                    Notification(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp), navController = navController,
                        billsViewModel = billsViewModel,
                        notificationViewModel = notificationViewModel
                    )

                }

                composable(
                    route = Screen.Language.route
                ) {

                    Language(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp), navController = navController
                    )

                }

//            composable(
//                route = Screen.EditQuestion.route
//            ) {
//                val selectedQuestion =
//                    navController.previousBackStackEntry?.savedStateHandle?.get<Question>("currentQuestion")
//
//                if (selectedQuestion != null) {
//                    EditQuestion(
//                        modifier = Modifier
//                            .fillMaxSize().padding(20.dp)
//                            ,
//                        navController = navController,
//                        questionsViewModel = questionsViewModel
//                    )
//                }
//            }

                composable(
                    route = Screen.HelpAndSupport.route
                ) {

                    HelpAndSupport(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        navController = navController,
                        questionsViewModel = questionsViewModel
                    )

                }
                composable(
                    route = Screen.AddExpenses.route
                ) {

                    AddExpensesScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        navController = navController,
                        transactionViewModel = transactionsViewModel,
                        catViewModel = categoryViewModel,
                        walletViewModel = walletViewModel,
                        window = window
                    )

                }

                composable(
                    route = Screen.AddIncomes.route
                ) {

                    AddIncomeScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        navController = navController,
                        transactionViewModel = transactionsViewModel,
                        catViewModel = categoryViewModel,
                        walletViewModel = walletViewModel,
                        window = window
                    )

                }


                composable(
                    route = Screen.AddCategory.route
                ) {

                    AddCategoryScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        catViewModel = categoryViewModel,
                        window = window
                    )

                }

                composable(
                    route = Screen.CategoryDetail.route,
                ) {


                    val selectedCategory =
                        navController.previousBackStackEntry?.savedStateHandle?.get<Category>("currentCategory")

                    if (selectedCategory != null) {

                        CategoryDetail(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            category = selectedCategory,
                            catViewModel = categoryViewModel
                        )

                    }

                }


                composable(
                    route = Screen.TransactionDetails.route,
                ) {


                    val selectedTransactions =
                        navController.previousBackStackEntry?.savedStateHandle?.get<Transactions>("currentTransaction")

                    if (selectedTransactions != null) {

                        TransactionDetail(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            transactions = selectedTransactions,
                            transactionViewModel = transactionsViewModel
                        )

                    }

                }

                composable(
                    route = Screen.BudgetScreen.route,
                ) {

                    BudgetScreen(
                        budgetViewModel = targetViewModel, window = window
                    )


                }

                composable(
                    route = Screen.AllTransaction.route,
                ) {

                    AllTransactionScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        transactionViewModel = transactionsViewModel,
                        dateViewModel = dateViewModel,
                        navController = navController, window = window
                    )


                }

                composable(
                    route = Screen.StaffScreen.route,
                ) {

                    StaffScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        staffViewModel = staffViewModel,
                        navController = navController,
                        walletViewModel = walletViewModel,
                        window = window
                    )


                }

                composable(
                    route = Screen.StaffDetailScreen.route,
                ) {

                    val selectedStaff =
                        navController.previousBackStackEntry?.savedStateHandle?.get<Staff>("currentStaff")

                    if (selectedStaff != null) {

                        StaffDetailScreen(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            staffViewModel = staffViewModel,
                            staff = selectedStaff
                        )

                    }


                }

                composable(
                    route = Screen.AddStaffScreen.route,
                ) {

                    StaffAddScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        staffViewModel = staffViewModel
                    )


                }
            }
        }
    }


}

val items = listOf(
    Screen.Overview, Screen.Wallet, Screen.Analysis, Screen.Settings
)


@ExperimentalCoroutinesApi
@Composable
fun connectivityStatus(): State<ConnectionStatus> {
    val context = LocalContext.current

    return produceState(initialValue = context.currentConnectivityStatus) {
        Log.d("ConnectivityStatus", "New connection status: $value")
        context.observeConnectivityAsFlow().collect { value = it }
    }
}

