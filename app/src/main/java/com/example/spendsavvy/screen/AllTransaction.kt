package com.example.spendsavvy.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.spendsavvy.viewModels.OverviewViewModel

@Composable
fun AllTransactionScreen(
    modifier: Modifier = Modifier,
    transactionViewModel: OverviewViewModel,
    navController: NavController
) {

    val transactionList by transactionViewModel.transactionsList.observeAsState(initial = emptyList())

    val sortedTransactions = transactionList.sortedBy { it.date }

    Column(
        modifier = modifier
    ) {


        TransactionList(
            sortedTransactions,
            navController = navController,
            transactionViewModel = transactionViewModel
        )

    }

}