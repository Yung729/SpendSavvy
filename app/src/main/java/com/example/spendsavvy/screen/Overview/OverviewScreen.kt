package com.example.spendsavvy.screen.Overview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.spendsavvy.R
import com.example.spendsavvy.components.LoadingScreen
import com.example.spendsavvy.components.SwipeToDeleteItem
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.models.UserData
import com.example.spendsavvy.navigation.Screen
import com.example.spendsavvy.ui.theme.HeaderTitle
import com.example.spendsavvy.ui.theme.ScreenSize
import com.example.spendsavvy.ui.theme.WindowType
import com.example.spendsavvy.ui.theme.poppinsFontFamily
import com.example.spendsavvy.viewModels.DateSelectionViewModel
import com.example.spendsavvy.viewModels.OverviewViewModel
import com.example.spendsavvy.viewModels.ProfileViewModel
import com.example.spendsavvy.viewModels.TargetViewModel
import com.example.spendsavvy.viewModels.WalletViewModel
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
fun OverviewScreen(
    modifier: Modifier = Modifier,
    transactionViewModel: OverviewViewModel,
    budgetViewModel: TargetViewModel,
    dateViewModel: DateSelectionViewModel,
    profileViewModel: ProfileViewModel,
    walletViewModel : WalletViewModel,
    navController: NavController,
    window: ScreenSize
) {
    val isLoading by transactionViewModel.isLoading.observeAsState(initial = false)

    val transactionList by transactionViewModel.selectedDateTransaction.observeAsState(initial = emptyList())
    val totalExpenses by transactionViewModel.selectedDateExpensesTotal.observeAsState(initial = 0.0)
    val totalIncomes by transactionViewModel.selectedDateIncomesTotal.observeAsState(initial = 0.0)
    val budgetAmount by budgetViewModel.budget.observeAsState(initial = 0.0)
    val goalAmount by budgetViewModel.goal.observeAsState(initial = 0.0)

    //wallet
    val totalCashAmount by walletViewModel.totalCashAmount.observeAsState(initial = 0.0)
    val totalFixedDeposit by walletViewModel.totalFixedDeposit.observeAsState(initial = 0.0)
    val totalPriceStock by walletViewModel.totalPriceStock.observeAsState(initial = 0.0)

    val userData by profileViewModel.userData.observeAsState(initial = UserData())

    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    val calendarState = rememberSheetState()
    val selectedDate = rememberSaveable {
        mutableStateOf(Calendar.getInstance().apply {
            time = Date()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time)
    }

    val updateSelectedDate: (Date) -> Unit = { newDate ->
        selectedDate.value = newDate
        dateViewModel.setSelectedDate(newDate)
    }

    val dismissStateMap = remember { mutableMapOf<Transactions, DismissState>() }


    LaunchedEffect(selectedDate.value) {
        dateViewModel.setSelectedDate(selectedDate.value)
    }


    if (isLoading) {
        LoadingScreen() // Display loading animation
    }

    Box(modifier = Modifier.fillMaxSize()) {


        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Transparent
        ) {
            CalendarDialog(
                state = calendarState,
                config = CalendarConfig(
                    monthSelection = true,
                    yearSelection = true, // Disable year selection
                    style = CalendarStyle.MONTH
                ),
                selection = CalendarSelection.Date { localDate ->
                    val calendar = Calendar.getInstance()
                    calendar.time =
                        Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    selectedDate.value = calendar.time
                    updateSelectedDate(calendar.time)
                    transactionViewModel.updateTransactions()
                }
            )
        }

        when (window.height) {
            WindowType.Expanded -> {
                LazyColumn(
                    modifier = modifier,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {


                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Hi, ${userData.userName}", //current User
                                        fontFamily = poppinsFontFamily,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = HeaderTitle
                                    )

                                }

                                Column(
                                    modifier = Modifier
                                        .clickable(onClick = { navController.navigate(Screen.Wallet.route) })
                                ) {

                                    Row {
                                        Image(
                                            painter = painterResource(id = R.drawable.wallet1),
                                            contentDescription = "wallet",
                                            modifier = Modifier.size(20.dp, 20.dp)
                                        )

                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = "RM ${totalCashAmount + totalFixedDeposit + totalPriceStock}",
                                            fontSize = 16.sp,
                                            color = Color.Black
                                        )
                                    }
                                }


                            }
                        }
                    }


                    item {
                        val selectedDateString = selectedDate.value.let {
                            if (dateFormat.format(it) == dateFormat.format(Date())) {
                                stringResource(id = R.string.today)
                            } else {
                                dateFormat.format(it)
                            }
                        } ?: "Select Month"





                        Row(
                            modifier = Modifier
                                .clickable(onClick = { calendarState.show() })
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.calendar),
                                contentDescription = "Calendar",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = selectedDateString,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }


                    }

                    item {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            OverViewCard(
                                incomes = totalIncomes,
                                expenses = totalExpenses,
                                window = window
                            )

                            Column(
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                            ) {

                                BudgetCard(
                                    budgetAmount = budgetAmount,
                                    totalExpenses = totalExpenses,
                                    navController = navController,
                                    window = window
                                )


                                Spacer(modifier = Modifier.height(20.dp))

                                GoalCard(
                                    goalAmount = goalAmount,
                                    totalIncomes = totalIncomes,
                                    totalExpenses = totalExpenses,
                                    navController = navController,
                                    window = window
                                )
                            }
                        }

                    }



                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(id = R.string.record),
                                fontFamily = poppinsFontFamily,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = HeaderTitle
                            )

                            ClickableText(
                                text = AnnotatedString(stringResource(id = R.string.seeAll)),
                                onHover = {
                                    TextStyle(
                                        fontFamily = poppinsFontFamily,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF0B2E41),
                                    )
                                },
                                onClick = {
                                    navController.navigate(Screen.AllTransaction.route)
                                },
                                modifier = Modifier.align(Alignment.CenterVertically),
                                style = TextStyle(
                                    fontFamily = poppinsFontFamily,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF489FCD),
                                )
                            )

                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        TransactionList(
                            modifier = Modifier.height(400.dp),
                            transactionsList = transactionList,
                            navController = navController,
                            transactionViewModel = transactionViewModel,
                            dismissStateMap = dismissStateMap
                        )
                    }


                }
            }

            else -> {
                LazyColumn(
                    modifier = modifier,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {

                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Hi, ${userData.userName}", //current User
                                        fontFamily = poppinsFontFamily,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = HeaderTitle
                                    )

                                }

                                Column(
                                    modifier = Modifier
                                        .clickable(onClick = { navController.navigate(Screen.Wallet.route) })
                                ) {

                                    Row {
                                        Image(
                                            painter = painterResource(id = R.drawable.wallet1),
                                            contentDescription = "wallet",
                                            modifier = Modifier.size(20.dp, 20.dp)
                                        )

                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = "RM ${totalCashAmount + totalFixedDeposit + totalPriceStock}",
                                            fontSize = 16.sp,
                                            color = Color.Black
                                        )
                                    }
                                }


                            }
                        }
                    }


                    item {
                        val selectedDateString = selectedDate.value.let {
                            if (dateFormat.format(it) == dateFormat.format(Date())) {
                                stringResource(id = R.string.today)
                            } else {
                                dateFormat.format(it)
                            }
                        } ?: "Select Month"





                        Row(
                            modifier = Modifier
                                .clickable(onClick = { calendarState.show() })
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.calendar),
                                contentDescription = "Calendar",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = selectedDateString,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }


                    }

                    item {
                        OverViewCard(
                            incomes = totalIncomes,
                            expenses = totalExpenses,
                            window = window
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            BudgetCard(
                                budgetAmount = budgetAmount,
                                totalExpenses = totalExpenses,
                                navController = navController,
                                window = window
                            )
                            GoalCard(
                                goalAmount = goalAmount,
                                totalIncomes = totalIncomes,
                                totalExpenses = totalExpenses,
                                navController = navController,
                                window = window
                            )
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(id = R.string.record),
                                fontFamily = poppinsFontFamily,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = HeaderTitle
                            )

                            ClickableText(
                                text = AnnotatedString(stringResource(id = R.string.seeAll)),
                                onHover = {
                                    TextStyle(
                                        fontFamily = poppinsFontFamily,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF0B2E41),
                                    )
                                },
                                onClick = {
                                    navController.navigate(Screen.AllTransaction.route)
                                },
                                modifier = Modifier.align(Alignment.CenterVertically),
                                style = TextStyle(
                                    fontFamily = poppinsFontFamily,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF489FCD),
                                )
                            )

                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        TransactionList(
                            modifier = Modifier.height(400.dp),
                            transactionsList = transactionList,
                            navController = navController,
                            transactionViewModel = transactionViewModel,
                            dismissStateMap = dismissStateMap
                        )
                    }


                }
            }
        }


    }

}


@Composable
fun OverViewCard(
    incomes: Double,
    expenses: Double,
    window: ScreenSize
) {

    when (window.height) {
        WindowType.Expanded -> {
            Card(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                ), shape = RoundedCornerShape(16.dp), modifier = Modifier

            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF696161), // Start color
                                    Color(0xFF1B1B1B)  // End color
                                )
                            )
                        )
                        .wrapContentWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .width(350.dp)
                            .height(260.dp)

                    ) {
                        Text(
                            text = stringResource(id = R.string.overview),
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Column(

                        ) {

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = painterResource(id = R.drawable.incomes),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(40.dp, 40.dp)
                                            .padding(end = 10.dp)
                                    )

                                    Column {
                                        Text(
                                            text = stringResource(id = R.string.income),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 25.sp,
                                            color = Color.White
                                        )

                                        Text(
                                            text = "RM $incomes",
                                            fontSize = 16.sp,
                                            color = Color.White
                                        )
                                    }

                                }


                            }

                            Spacer(modifier = Modifier.height(40.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = painterResource(id = R.drawable.expenses),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(40.dp, 40.dp)
                                            .padding(end = 10.dp)
                                    )

                                    Column {
                                        Text(
                                            text = stringResource(id = R.string.expense),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 25.sp,
                                            color = Color.White
                                        )

                                        Text(
                                            text = "RM $expenses",
                                            fontSize = 16.sp,
                                            color = Color.White
                                        )
                                    }

                                }


                            }


                        }
                    }
                }
            }
        }

        else -> {
            Card(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                ), shape = RoundedCornerShape(16.dp), modifier = Modifier
                    .fillMaxWidth()

            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF696161), // Start color
                                    Color(0xFF1B1B1B)  // End color
                                )
                            )
                        )
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(10.dp)

                    ) {
                        Text(
                            text = stringResource(id = R.string.overview),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = painterResource(id = R.drawable.incomes),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(30.dp, 30.dp)
                                            .padding(end = 10.dp)
                                    )

                                    Column {
                                        Text(
                                            text = stringResource(id = R.string.income),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = Color.White
                                        )

                                        Text(
                                            text = "RM $incomes",
                                            fontSize = 14.sp,
                                            color = Color.White
                                        )
                                    }

                                }


                            }

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = painterResource(id = R.drawable.expenses),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(30.dp, 30.dp)
                                            .padding(end = 10.dp)
                                    )

                                    Column {
                                        Text(
                                            text = stringResource(id = R.string.expense),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = Color.White
                                        )

                                        Text(
                                            text = "RM $expenses",
                                            fontSize = 14.sp,
                                            color = Color.White
                                        )
                                    }

                                }


                            }


                        }
                    }
                }
            }
        }
    }


}

@Composable
fun BudgetCard(
    budgetAmount: Double,
    totalExpenses: Double,
    navController: NavController,
    window: ScreenSize
) {

    when (window.height) {
        WindowType.Expanded -> {
            Card(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier

            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF696161), // Start color
                                    Color(0xFF1B1B1B)  // End color
                                )
                            )
                        )
                        .wrapContentWidth()

                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .width(300.dp)
                            .clickable(onClick = { navController.navigate(Screen.BudgetScreen.route) })
                    ) {
                        Text(
                            text = stringResource(id = R.string.budget),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "RM $budgetAmount",
                            modifier = Modifier,
                            color = Color.White

                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(id = R.string.availableBalance) + " : RM ${budgetAmount - totalExpenses}",
                            fontSize = 16.sp,
                            color = Color.White
                        )


                    }
                }
            }
        }

        else -> {
            Card(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF696161), // Start color
                                    Color(0xFF1B1B1B)  // End color
                                )
                            )
                        )
                        .fillMaxWidth()

                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable(onClick = { navController.navigate(Screen.BudgetScreen.route) })
                    ) {
                        Text(
                            text = stringResource(id = R.string.budget),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "RM $budgetAmount",
                            modifier = Modifier,
                            color = Color.White

                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(id = R.string.availableBalance) + " : RM ${budgetAmount - totalExpenses}",
                            fontSize = 16.sp,
                            color = Color.White
                        )


                    }
                }
            }
        }
    }


}

@Composable
fun GoalCard(
    goalAmount: Double,
    totalIncomes: Double,
    totalExpenses: Double,
    navController: NavController,
    window: ScreenSize
) {

    when (window.height) {
        WindowType.Expanded -> {
            Card(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier

            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF696161), // Start color
                                    Color(0xFF1B1B1B)  // End color
                                )
                            )
                        )
                        .wrapContentWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .width(300.dp)
                            .clickable(onClick = { navController.navigate(Screen.BudgetScreen.route) })
                    ) {
                        Text(
                            text = stringResource(id = R.string.goal),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "RM $goalAmount",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(id = R.string.balanceNeeded) + ": RM ${goalAmount - (totalIncomes - totalExpenses)}",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }


            }
        }

        else -> {
            Card(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF696161), // Start color
                                    Color(0xFF1B1B1B)  // End color
                                )
                            )
                        )
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable(onClick = { navController.navigate(Screen.BudgetScreen.route) })
                    ) {
                        Text(
                            text = stringResource(id = R.string.goal),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "RM $goalAmount",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(id = R.string.balanceNeeded) + ": RM ${goalAmount - (totalIncomes - totalExpenses)}",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }


            }
        }
    }


}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TransactionsCard(
    transactions: Transactions,
    modifier: Modifier = Modifier,
    navController: NavController,
    transactionViewModel: OverviewViewModel,
    dismissState: DismissState
) {

    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())




    SwipeToDeleteItem(state = dismissState) {
        Card(
            modifier = modifier.clickable {
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    key = "currentTransaction", value = transactions
                )

                navController.navigate(Screen.TransactionDetails.route)
            }, colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {

            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically

            ) {

                Image(
                    painter = rememberAsyncImagePainter(model = transactions.category.imageUri),
                    contentDescription = "",
                    modifier = Modifier
                        .size(30.dp, 30.dp)
                        .padding(end = 10.dp)
                )

                Column(
                    horizontalAlignment = Alignment.Start, modifier = Modifier
                ) {
                    Text(
                        text = transactions.category.categoryName,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = dateFormat.format(transactions.date), fontSize = 10.sp
                    )
                }


                Text(
                    text = transactions.amount.toString(),
                    fontWeight = FontWeight.SemiBold,
                    color = if (transactions.transactionType == "Expenses") Color.Red else Color(
                        0xFF119316
                    ),
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }


        if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
            transactionViewModel.deleteTransaction(transactions)
        }
    }


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TransactionList(
    transactionsList: List<Transactions>,
    modifier: Modifier = Modifier,
    navController: NavController,
    transactionViewModel: OverviewViewModel,
    dismissStateMap: MutableMap<Transactions, DismissState>
) {
    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    var lastDate = ""
    val todayDate = Calendar.getInstance().time

    LazyColumn(modifier = modifier) {
        items(transactionsList) { item: Transactions ->

            if (dateFormat.format(item.date) != lastDate) {

                if (dateFormat.format(todayDate) == dateFormat.format(item.date)) {
                    Text(stringResource(id = R.string.today), fontWeight = FontWeight.SemiBold)
                } else {
                    Text(text = dateFormat.format(item.date), fontWeight = FontWeight.SemiBold)
                }

            }

            TransactionsCard(
                transactions = item,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                navController = navController,
                transactionViewModel = transactionViewModel,
                dismissState = dismissStateMap[item]
                    ?: rememberDismissState().also { dismissStateMap[item] = it }

            )

            lastDate = dateFormat.format(item.date)
        }
    }
}
