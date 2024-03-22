package com.example.spendsavvy.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.ui.theme.GreenColor
import com.example.spendsavvy.ui.theme.HeaderTitle
import com.example.spendsavvy.ui.theme.poppinsFontFamily

@Composable
fun WalletScreen(modifier: Modifier = Modifier, navController: NavController) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Wallet",
                fontFamily = poppinsFontFamily,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = HeaderTitle,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Icon(
                Icons.Default.Notifications,
                contentDescription = "Notifications",
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White, //later need change to brush color
                contentColor = Color.Black
            ), elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            ), shape = RoundedCornerShape(15.dp), modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(15.dp)) {
                Text(text = "Available Balance")

                Text(text = "RM 9,000")
            }
        }

        Spacer(modifier = Modifier.height(35.dp))

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Assets",
                fontFamily = poppinsFontFamily,
                fontSize = 15.sp,
                color = HeaderTitle,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Text(
                text = "RM 9,000",
                fontFamily = poppinsFontFamily,
                fontSize = 15.sp,
                color = GreenColor,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Text(
                    text = "Money",
                    fontFamily = poppinsFontFamily,
                    fontSize = 15.sp,
                    color = HeaderTitle
                )

                Text(
                    text = "Includes Cash Money , Bank and Ewallets",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }


            OutlinedButton(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(3.dp),
                border = BorderStroke(1.dp, Color.Black)

            ) {
                Text(
                    text = "Add Transactions",
                    color = Color.Black,
                    fontSize = 10.sp
                )
            }

        }
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Cash Money"

                )

                Text(
                    text = "Available balance",
                    color = Color.Gray,
                    fontSize = 10.sp
                )
            }
            Text(text = "RM5000")
        }
        Spacer(modifier = Modifier.height(5.dp))
        Divider(color = Color.Gray, thickness = 0.7.dp)
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Bank Accounts"
                )
            }
            Text(text = "2 accounts")
        }

        Spacer(modifier = Modifier.height(20.dp))
        Divider(color = Color.Gray, thickness = 0.7.dp)

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Text(
                    text = "Fixed Deposit",
                    fontFamily = poppinsFontFamily,
                    fontSize = 15.sp,
                    color = HeaderTitle
                )

                Text(
                    text = "Assets that Earn Interest",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }


            OutlinedButton(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(3.dp),
                border = BorderStroke(1.dp, Color.Black)

            ) {
                Text(
                    text = "Add/Manage FD",
                    color = Color.Black,
                    fontSize = 10.sp
                )
            }

        }
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "FD Account"
                )

            }
            Text(text = "3 accounts")
        }

        Spacer(modifier = Modifier.height(20.dp))
        Divider(color = Color.Gray, thickness = 0.7.dp)
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Text(
                    text = "Stock",
                    fontFamily = poppinsFontFamily,
                    fontSize = 15.sp,
                    color = HeaderTitle
                )

                Text(
                    text = "Add and sell stocks",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }


            OutlinedButton(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(3.dp),
                border = BorderStroke(1.dp, Color.Black)

            ) {
                Text(
                    text = "Add/Manage Stock",
                    color = Color.Black,
                    fontSize = 10.sp
                )
            }

        }

        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Stocks"
                )

            }
            Text(text = "5 stocks")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun WalletScreenPreview() {
    WalletScreen(
        Modifier
            .fillMaxSize()
            .padding(20.dp), navController = rememberNavController()
    )

}