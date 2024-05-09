package com.example.spendsavvy.screen.Overview

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun AllTransactionScreen(
    modifier: Modifier = Modifier,
    transactionViewModel: OverviewViewModel,
    dateViewModel: DateSelectionViewModel,
    navController: NavController
) {

    val allTransactionList by transactionViewModel.transactionsList.observeAsState(initial = emptyList())
    val dayTransactionList by transactionViewModel.todayTransactionsList.observeAsState(initial = emptyList())
    val monthTransactionList by transactionViewModel.monthTransactionsList.observeAsState(initial = emptyList())
    val yearTransactionList by transactionViewModel.yearTransactionsList.observeAsState(initial = emptyList())
    val customTransactionList by transactionViewModel.selectedDateRangeTransaction.observeAsState(
        initial = emptyList()
    )

    val allSortedTransactions = allTransactionList.sortedByDescending { it.date }
    val monthSortedTransactions = monthTransactionList.sortedByDescending { it.date }
    val yearSortedTransactions = yearTransactionList.sortedByDescending { it.date }

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

    val options = mutableStateListOf("Year", "Month", "Day", "ALL", "Custom")
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
                OverViewCard(incomes = yearIncomes, expenses = yearExpenses)
                Spacer(modifier = Modifier.height(10.dp))
                TransactionList(
                    yearSortedTransactions,
                    navController = navController,
                    transactionViewModel = transactionViewModel
                )
            }

            1 -> {
                OverViewCard(incomes = monthIncomes, expenses = monthExpenses)
                Spacer(modifier = Modifier.height(10.dp))
                TransactionList(
                    monthSortedTransactions,
                    navController = navController,
                    transactionViewModel = transactionViewModel
                )
            }

            2 -> {
                OverViewCard(incomes = todayIncomes, expenses = todayExpenses)
                Spacer(modifier = Modifier.height(10.dp))
                TransactionList(
                    dayTransactionList,
                    navController = navController,
                    transactionViewModel = transactionViewModel
                )
            }

            3 -> {
                OverViewCard(incomes = allIncomes, expenses = allExpenses)
                Spacer(modifier = Modifier.height(10.dp))
                TransactionList(
                    allSortedTransactions,
                    navController = navController,
                    transactionViewModel = transactionViewModel
                )
            }

            4 -> {
                OverViewCard(incomes = dateRangeIncomes, expenses = dateRangeExpenses)
                Spacer(modifier = Modifier.height(10.dp))
                TransactionList(
                    customTransactionList,
                    navController = navController,
                    transactionViewModel = transactionViewModel
                )
            }
        }


    }

}