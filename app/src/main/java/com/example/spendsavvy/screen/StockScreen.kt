package com.example.spendsavvy.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendsavvy.animation.bounceClick
import com.example.spendsavvy.data.StockData
import com.example.spendsavvy.State.Stock
import com.example.spendsavvy.ui.theme.poppinsFontFamily

@Composable
fun StockScreen(modifier: Modifier = Modifier) {

    // Calculate total stock balance
    val totalStockBalance = remember {
        StockData().loadStock().sumOf { it.originalPrice * it.quantity }
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                /*HeaderTitle(text = "Stock Account")*/
                Text(
                    text = "Add and Sell Stocks",
                    color = Color.Gray,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }

        Spacer(modifier = Modifier.height(30.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White, //later need change to brush color
                contentColor = Color.Black
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            ),
            shape = RoundedCornerShape(15.dp), modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(15.dp)) {
                Text(
                    text = "Stock Balance",
                    fontWeight = FontWeight.SemiBold
                )

                Text(text = "RM $totalStockBalance")
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Stock",
            fontFamily = poppinsFontFamily,
            fontSize = 15.sp,
            color = com.example.spendsavvy.ui.theme.HeaderTitle
        )

        Box(modifier = Modifier.fillMaxSize()) {
            StockList(stockList = StockData().loadStock())

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .bounceClick()
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "Add Stock",
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }

                    Button(
                        onClick = { },
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .bounceClick()
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Sell Stock",
                            textAlign = TextAlign.Center,
                            color = Color.Black
                        )
                    }
                }
            }
        }


    }


}

@Composable
fun StockCard(stock: Stock, modifier: Modifier = Modifier) {

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
                painter = painterResource(id = stock.productImage),
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
                    text = stock.productName,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "RM ${(stock.originalPrice * stock.quantity)}",
                    fontSize = 10.sp
                )
            }


            Text(
                text = "Qty : ${stock.quantity}",
                fontWeight = FontWeight.SemiBold,
                color = Color.Green,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

    }
}

@Composable
fun StockList(stockList: List<Stock>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(stockList) { item: Stock ->
            StockCard(
                stock = item,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StockScreenPreview() {
    StockScreen(
        Modifier
            .fillMaxSize()
            .padding(20.dp)
    )

}