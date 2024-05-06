package com.example.spendsavvy.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.spendsavvy.viewModels.OverviewViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun AllTransactionScreen(
    modifier: Modifier = Modifier,
    transactionViewModel: OverviewViewModel,
    navController: NavController
) {

    val allTransactionList by transactionViewModel.transactionsList.observeAsState(initial = emptyList())
    val dayTransactionList by transactionViewModel.todayTransactionsList.observeAsState(initial = emptyList())
    val monthTransactionList by transactionViewModel.monthTransactionsList.observeAsState(initial = emptyList())
    val yearTransactionList by transactionViewModel.yearTransactionsList.observeAsState(initial = emptyList())

    val allSortedTransactions = allTransactionList.sortedByDescending { it.date }
    val monthSortedTransactions = monthTransactionList.sortedByDescending { it.date }
    val yearSortedTransactions = yearTransactionList.sortedByDescending { it.date }


    val options = mutableStateListOf("Year", "Month", "Day", "ALL")
    var selectedIndex by remember {
        mutableIntStateOf(3)
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
                        onClick = { selectedIndex = index },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index, count = options.size
                        )
                    ) {
                        Text(text = option)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        when (selectedIndex) {
            0 -> {
                TransactionList(
                    yearSortedTransactions,
                    navController = navController,
                    transactionViewModel = transactionViewModel
                )
            }

            1 -> {

                TransactionList(
                    monthSortedTransactions,
                    navController = navController,
                    transactionViewModel = transactionViewModel
                )
            }

            2 -> {

                TransactionList(
                    dayTransactionList,
                    navController = navController,
                    transactionViewModel = transactionViewModel
                )
            }

            3 -> {
                TransactionList(
                    allSortedTransactions,
                    navController = navController,
                    transactionViewModel = transactionViewModel
                )
            }
        }


    }

}