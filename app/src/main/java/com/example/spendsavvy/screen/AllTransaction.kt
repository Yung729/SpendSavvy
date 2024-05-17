package com.example.spendsavvy.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spendsavvy.R
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.ui.theme.ScreenSize
import com.example.spendsavvy.ui.theme.WindowType
import com.example.spendsavvy.viewModels.DateSelectionViewModel
import com.example.spendsavvy.viewModels.OverviewViewModel
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun AllTransactionScreen(
    modifier: Modifier = Modifier,
    transactionViewModel: OverviewViewModel,
    dateViewModel: DateSelectionViewModel,
    navController: NavController,
    window: ScreenSize
) {

    val allTransactionList by transactionViewModel.transactionsList.observeAsState(initial = emptyList())
    val dayTransactionList by transactionViewModel.todayTransactionsList.observeAsState(initial = emptyList())
    val monthTransactionList by transactionViewModel.monthTransactionsList.observeAsState(initial = emptyList())
    val yearTransactionList by transactionViewModel.yearTransactionsList.observeAsState(initial = emptyList())
    val customTransactionList by transactionViewModel.selectedDateRangeTransaction.observeAsState(
        initial = emptyList()
    )

    val allSortedTransactions = allTransactionList.sortedByDescending { it.date }
    val daySortedTransactions = dayTransactionList.sortedByDescending { it.date }
    val monthSortedTransactions = monthTransactionList.sortedByDescending { it.date }
    val yearSortedTransactions = yearTransactionList.sortedByDescending { it.date }
    val customSortedTransactions = customTransactionList.sortedByDescending { it.date }

    val todayExpenses by transactionViewModel.todayExpensesTotal.observeAsState(initial = 0.0)
    val todayIncomes by transactionViewModel.todayIncomesTotal.observeAsState(initial = 0.0)

    val allExpenses by transactionViewModel.expensesTotal.observeAsState(initial = 0.0)
    val allIncomes by transactionViewModel.incomesTotal.observeAsState(initial = 0.0)

    val monthExpenses by transactionViewModel.currentMonthExpenses.observeAsState(initial = 0.0)
    val monthIncomes by transactionViewModel.currentMonthIncomes.observeAsState(initial = 0.0)

    val yearExpenses by transactionViewModel.currentYearExpensesTotal.observeAsState(initial = 0.0)
    val yearIncomes by transactionViewModel.currentYearIncomesTotal.observeAsState(initial = 0.0)

    val dateRangeExpenses by transactionViewModel.dateRangeExpensesTotal.observeAsState(initial = 0.0)
    val dateRangeIncomes by transactionViewModel.dateRangeIncomesTotal.observeAsState(initial = 0.0)

    val options = mutableStateListOf(
        stringResource(id = R.string.year),
        stringResource(id = R.string.month),
        stringResource(id = R.string.day),
        stringResource(id = R.string.all),
        stringResource(id = R.string.custom)
    )
    var selectedIndex by remember {
        mutableIntStateOf(3)
    }

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

    val dismissStateMap = remember { mutableMapOf<Transactions, DismissState>() }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Transparent
    ) {
        CalendarDialog(
            state = calendarState,
            config = CalendarConfig(
                monthSelection = true,
                yearSelection = true,
                style = CalendarStyle.MONTH
            ),
            selection = CalendarSelection.Period { startDate, endDate ->
                val start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                val end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                val calendar = Calendar.getInstance().apply {
                    time = end
                    set(Calendar.HOUR_OF_DAY, 23)
                    set(Calendar.MINUTE, 59)
                    set(Calendar.SECOND, 59)
                    set(Calendar.MILLISECOND, 999)
                }.time
                dateViewModel.setStartDate(start)
                dateViewModel.setEndDate(calendar)
                transactionViewModel.updateTransactions()
            }
        )
    }

    Column(
        modifier = modifier
    ) {

        Row(
            horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()
        ) {
            SingleChoiceSegmentedButtonRow {
                options.forEachIndexed { index, option ->
                    SegmentedButton(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                            if (index == 4) {
                                calendarState.show()
                            }
                        },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index, count = options.size
                        )
                    ) {
                        Text(text = option, fontSize = 10.sp)
                    }
                }


            }
        }

        Spacer(modifier = Modifier.height(20.dp))


        when (selectedIndex) {
            0 -> {
                DetailCard(incomes = yearIncomes, expenses = yearExpenses, window = window)
                Spacer(modifier = Modifier.height(10.dp))
                TransactionList(
                    yearSortedTransactions,
                    navController = navController,
                    transactionViewModel = transactionViewModel,
                    dismissStateMap = dismissStateMap
                )
            }

            1 -> {
                DetailCard(incomes = monthIncomes, expenses = monthExpenses, window = window)
                Spacer(modifier = Modifier.height(10.dp))
                TransactionList(
                    monthSortedTransactions,
                    navController = navController,
                    transactionViewModel = transactionViewModel,
                    dismissStateMap = dismissStateMap
                )
            }

            2 -> {
                DetailCard(incomes = todayIncomes, expenses = todayExpenses, window = window)
                Spacer(modifier = Modifier.height(10.dp))
                TransactionList(
                    daySortedTransactions,
                    navController = navController,
                    transactionViewModel = transactionViewModel,
                    dismissStateMap = dismissStateMap
                )
            }

            3 -> {
                DetailCard(incomes = allIncomes, expenses = allExpenses, window = window)
                Spacer(modifier = Modifier.height(10.dp))
                TransactionList(
                    allSortedTransactions,
                    navController = navController,
                    transactionViewModel = transactionViewModel,
                    dismissStateMap = dismissStateMap
                )
            }

            4 -> {
                DetailCard(
                    incomes = dateRangeIncomes,
                    expenses = dateRangeExpenses,
                    window = window
                )
                Spacer(modifier = Modifier.height(10.dp))
                TransactionList(
                    customSortedTransactions,
                    navController = navController,
                    transactionViewModel = transactionViewModel,
                    dismissStateMap = dismissStateMap
                )
            }
        }


    }

}

@Composable
fun DetailCard(
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
                            .padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.overview),
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = painterResource(id = R.drawable.incomes),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(35.dp, 35.dp)
                                            .padding(end = 10.dp)
                                    )

                                    Column {
                                        Text(
                                            text = stringResource(id = R.string.income),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp,
                                            color = Color.White
                                        )

                                        Text(
                                            text = String.format("RM %.2f", expenses),
                                            fontSize = 18.sp,
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
                                            .size(35.dp, 35.dp)
                                            .padding(end = 10.dp)
                                    )

                                    Column {
                                        Text(
                                            text = stringResource(id = R.string.expense),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp,
                                            color = Color.White
                                        )

                                        Text(
                                            text = String.format("RM %.2f", expenses),
                                            fontSize = 18.sp,
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
                            text = "Overview",
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
                                            text = "Income",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = Color.White
                                        )

                                        Text(
                                            text = String.format("RM %.2f", incomes),
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
                                            text = "Expenses",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = Color.White
                                        )

                                        Text(
                                            text = String.format("RM %.2f", expenses),
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