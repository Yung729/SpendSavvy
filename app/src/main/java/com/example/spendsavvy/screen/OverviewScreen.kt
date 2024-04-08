package com.example.spendsavvy.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendsavvy.data.TransactionData
import com.example.spendsavvy.State.Transactions
import com.example.spendsavvy.ui.theme.CardColor
import com.example.spendsavvy.ui.theme.HeaderTitle
import com.example.spendsavvy.ui.theme.poppinsFontFamily

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OverviewScreen(modifier: Modifier = Modifier) {


    Column(
        modifier = modifier
    ) {

        /*Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            HeaderTitle(text = "Overview")

            Icon(
                Icons.Default.Notifications,
                contentDescription = "Notifications",
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }*/


        Text(
            text = "Hi, User",
            fontFamily = poppinsFontFamily,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = HeaderTitle
        )

        OverViewCard()

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Record",
                fontFamily = poppinsFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = HeaderTitle
            )

            ClickableText(text = AnnotatedString("See All"),
                onHover = {

                },
                onClick = {

                },
                modifier = Modifier.align(Alignment.CenterVertically),
                style = TextStyle(
                    fontFamily = poppinsFontFamily,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Green,
                )
            )

        }

        TransactionList(TransactionData().loadTodayTransactions())

    }
}

@Composable
fun OverViewCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = CardColor,
            contentColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .fillMaxWidth()

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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
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

@Composable
fun TransactionsCard(transactions: Transactions, modifier: Modifier = Modifier) {

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(width = 1.dp, Color.Black)
    ) {

        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically

        ) {

            Image(
                painter = painterResource(id = transactions.category.imageResourceId),
                contentDescription = "",
                modifier = Modifier
                    .size(30.dp, 30.dp)
                    .padding(end = 10.dp)
            )

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
            ) {
                Text(
                    text = transactions.category.name,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = transactions.date.toString(),
                    fontSize = 10.sp
                )
            }


            Text(
                text = transactions.amount.toString(),
                fontWeight = FontWeight.SemiBold,
                color =
                if (transactions.category.isExpenses) Color.Red else Color.Green,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

    }
}

@Composable
fun TransactionList(transactionsList: List<Transactions>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(transactionsList) { item: Transactions ->
            TransactionsCard(
                transactions = item,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OverviewScreenPreview() {
    OverviewScreen(
        Modifier
            .fillMaxSize()
            .padding(20.dp)
    )
}