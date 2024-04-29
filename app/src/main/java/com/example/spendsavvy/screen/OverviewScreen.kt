package com.example.spendsavvy.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.DismissDirection
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.spendsavvy.components.SwipeToDeleteItem
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.navigation.Screen
import com.example.spendsavvy.ui.theme.CardColor
import com.example.spendsavvy.ui.theme.HeaderTitle
import com.example.spendsavvy.ui.theme.poppinsFontFamily
import com.example.spendsavvy.viewModels.OverviewViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OverviewScreen(
    modifier: Modifier = Modifier,
    transactionViewModel: OverviewViewModel,
    navController: NavController
) {

    val transactionList by transactionViewModel.todayTransactionsList.observeAsState(initial = emptyList())


    Column(
        modifier = modifier
    ) {

        Text(
            text = "Hi, User", //current User
            fontFamily = poppinsFontFamily,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = HeaderTitle
        )

        OverViewCard()

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Record",
                fontFamily = poppinsFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = HeaderTitle
            )

            ClickableText(text = AnnotatedString("See All"), onHover = {

            }, onClick = {
                navController.navigate(Screen.AllTransaction.route)
            }, modifier = Modifier.align(Alignment.CenterVertically), style = TextStyle(
                fontFamily = poppinsFontFamily,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Green,
            )
            )

        }

        TransactionList(
            transactionList,
            navController = navController,
            transactionViewModel = transactionViewModel
        )

    }

}


@Composable
fun OverViewCard() {


    Card(
        colors = CardDefaults.cardColors(
            containerColor = CardColor, contentColor = Color.White
        ), elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ), shape = RoundedCornerShape(15.dp), modifier = Modifier.fillMaxWidth()

    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = "Total Balance",
                modifier = Modifier,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )

            Text(
                text = "RM 5000.00",
                modifier = Modifier,
                textAlign = TextAlign.Center,

                )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column {
                    Text(
                        text = "Income",
                        modifier = Modifier,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )

                    Text(
                        text = "RM 6000.00",
                        modifier = Modifier,
                        textAlign = TextAlign.Center,
                    )
                }

                Column {
                    Text(
                        text = "Expenses",
                        modifier = Modifier,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )

                    Text(
                        text = "RM 1000.00",
                        modifier = Modifier,
                        textAlign = TextAlign.Center,
                    )
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
    transactionViewModel: OverviewViewModel
) {

    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    val dismissState = rememberDismissState()
    if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
        transactionViewModel.deleteTransaction(transactions)
    }

    if (!dismissState.isDismissed(DismissDirection.StartToEnd)) {


        SwipeToDeleteItem(state = dismissState) {
            Card(
                modifier = modifier.clickable {
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        key = "currentTransaction", value = transactions
                    )

                    navController.navigate(Screen.TransactionDetails.route)
                }, colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ), border = BorderStroke(width = 1.dp, Color.Black)
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
                        color = if (transactions.transactionType == "Expenses") Color.Red else Color.Green,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            }
        }
    }
}

@Composable
fun TransactionList(
    transactionsList: List<Transactions>,
    modifier: Modifier = Modifier,
    navController: NavController,
    transactionViewModel: OverviewViewModel
) {
    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    var lastDate : String = ""

    LazyColumn(modifier = modifier) {
        items(transactionsList) { item: Transactions ->

            if (dateFormat.format(item.date) != lastDate){
                Text(text = dateFormat.format(item.date))

                Divider()
            }

            TransactionsCard(
                transactions = item,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                navController = navController,
                transactionViewModel = transactionViewModel
            )

            lastDate = dateFormat.format(item.date)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OverviewScreenPreview() {
    OverviewScreen(
        Modifier
            .fillMaxSize()
            .padding(20.dp),
        transactionViewModel = viewModel(),
        navController = rememberNavController()
    )
}