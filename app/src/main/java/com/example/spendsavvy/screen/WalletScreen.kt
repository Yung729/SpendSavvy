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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.navigation.Screen
import com.example.spendsavvy.ui.theme.GreenColor
import com.example.spendsavvy.ui.theme.HeaderTitle
import com.example.spendsavvy.ui.theme.poppinsFontFamily
import com.example.spendsavvy.viewModels.WalletViewModel

@Composable
fun WalletScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    walletViewModel: WalletViewModel
) {
    var count by remember {
        mutableStateOf(0)
    }

    var bankCount by remember {
        mutableStateOf(0)
    }

    var fdCount by remember {
        mutableStateOf(0)
    }

    var stockCount by remember {
        mutableStateOf(0)
    }

    val cashDetailsList by walletViewModel.cashDetailsList.observeAsState(initial = emptyList())
    val fdAccDetailsList by walletViewModel.fdAccDetailsList.observeAsState(initial = emptyList())
    val stockListLive by walletViewModel.stockListLive.observeAsState(initial = emptyList())

    Column(
        modifier = modifier
    ) {
        /* Row(
             modifier = Modifier.fillMaxWidth(),
             horizontalArrangement = Arrangement.SpaceBetween
         ) {
             HeaderTitle(text = "Wallet")

             Icon(
                 Icons.Default.Notifications,
                 contentDescription = "Notifications",
                 modifier = Modifier.align(Alignment.CenterVertically)
             )
         }*/

        Spacer(modifier = Modifier.height(25.dp))

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
                Text(text = "Available Balance")

                Text(text = "RM 9,000")
            }
        }

        Spacer(modifier = Modifier.height(35.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Assets",
                fontFamily = poppinsFontFamily,
                fontSize = 15.sp,
                color = HeaderTitle,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Text(
                text = "RM $",
                fontFamily = poppinsFontFamily,
                fontSize = 15.sp,
                color = GreenColor,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Text(
                    text = "Cash",
                    fontFamily = poppinsFontFamily,
                    fontSize = 15.sp,
                    color = HeaderTitle
                )

                Text(
                    text = "Includes Cash Money and Bank",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }


            OutlinedButton(
                onClick = {
                    navController.navigate(route = Screen.Cash.route)
                },
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(3.dp),
                border = BorderStroke(1.dp, Color.Black)

            ) {
                Text(
                    text = "Add Cash Details",
                    color = Color.Black,
                    fontSize = 10.sp
                )
            }

        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Cash Money"
                )

                Text(
                    text = "Available balance",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }

            for (cashDetails in cashDetailsList) {
                if (cashDetails.typeName == "Cash") {
                    count = 1
                    Text(
                        text = "RM ${cashDetails.balance}",
                        fontFamily = poppinsFontFamily,
                        fontSize = 15.sp,
                        color = GreenColor,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    break
                }
            }
            if (count != 1)
                Text(
                    text = "RM 0.00"
                )

        }

        Spacer(modifier = Modifier.height(10.dp))
        Divider(color = Color.Gray, thickness = 0.7.dp)
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Bank Accounts"
            )


            for (cashAccount in cashDetailsList) {
                if (cashAccount.typeName != "Cash")
                    bankCount++
            }

            Text(
                text = "$bankCount Account(s)"
            )

        }

        Spacer(modifier = Modifier.height(10.dp))
        Divider(color = Color.Gray, thickness = 0.7.dp)

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
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
                onClick = {
                    navController.navigate(Screen.FixedDepositScreen.route)
                },
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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "FD Account"
            )

            for (fdAccount in fdAccDetailsList)
                fdCount++

            Text(text = "$fdCount accounts")
        }

        Spacer(modifier = Modifier.height(5.dp))
        Divider(color = Color.Gray, thickness = 0.7.dp)

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
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
                onClick = {
                    navController.navigate(route = Screen.Stock.route)
                },
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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Stocks"
            )

            for (stockDetails in stockListLive)
                stockCount++


            Text(
                text = "$stockCount stocks"
            )
        }

    }
}

/*
@Preview(showBackground = true)
@Composable
fun WalletScreenPreview() {
    WalletScreen(
        Modifier
            .fillMaxSize()
            .padding(20.dp), navController = rememberNavController()
    )

}*/
