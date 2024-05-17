package com.example.spendsavvy.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.spendsavvy.R
import com.example.spendsavvy.models.Cash
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.viewModels.OverviewViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CashTransactionScreen(
    modifier: Modifier,
    navController: NavController,
    cash: Cash,
    transactionViewModel: OverviewViewModel
) {

    val transactionList by transactionViewModel.transactionsList.observeAsState(emptyList())
    val cashTransactions =
        transactionList.filter { it.paymentMethod == cash.typeName }.sortedByDescending { it.date }

    Column(
        modifier = Modifier.padding(start = 15.dp)
    ) {

        Text(
            cash.typeName,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(15.dp))


        Row {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = rememberAsyncImagePainter(model = cash.imageUri),
                        contentDescription = "",
                        modifier = Modifier
                            .size(30.dp, 30.dp)
                            .padding(end = 10.dp)
                    )


                    Text(
                        "Account Balance",
                        color = Color.Gray
                    )

                    Text(text = String.format("RM %.2f",cash.balance))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "Record",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        CashTransactionList(cashTransactions)

    }
}

@Composable
fun CashTransactionList(
    transactionsList: List<Transactions>,
    modifier: Modifier = Modifier
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

            CashTransactionCard(
                transactions = item,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()

            )

            lastDate = dateFormat.format(item.date)
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CashTransactionCard(
    transactions: Transactions,
    modifier: Modifier = Modifier,
) {

    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())


    Card(

        colors = CardDefaults.cardColors(
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
                text = ("RM ${transactions.amount}"),
                fontWeight = FontWeight.SemiBold,
                color = if (transactions.transactionType == "Expenses") Color.Red else Color(
                    0xFF119316
                ),
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }


}