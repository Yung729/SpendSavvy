package com.example.spendsavvy.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.R
import com.example.spendsavvy.models.Cash
import com.example.spendsavvy.models.FDAccount
import com.example.spendsavvy.models.Stock
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

        Spacer(modifier = Modifier.height(25.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White, //later need change to brush color
                contentColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            ),
            shape = RoundedCornerShape(15.dp), modifier = Modifier.fillMaxWidth()
        ) {

            Box(
                modifier = Modifier.background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF696161), // Start color
                            Color(0xFF1B1B1B)  // End color
                        )
                    )
                ).fillMaxWidth()
            ){
            Column(modifier = Modifier.padding(15.dp)) {
                Text(stringResource(id = R.string.availableBalance))

                Text(text = "RM $")
            }
            }
        }

        Spacer(modifier = Modifier.height(35.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.assets),
                fontFamily = poppinsFontFamily,
                fontSize = 25.sp,
                color = HeaderTitle,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Text(
                text = "RM $",
                fontFamily = poppinsFontFamily,
                fontSize = 25.sp,
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
                    text = stringResource(id = R.string.cash),
                    fontFamily = poppinsFontFamily,
                    fontSize = 15.sp,
                    color = HeaderTitle
                )

                Text(
                    text = stringResource(id = R.string.text_3),
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
                    text = stringResource(id = R.string.text_4),
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
                    stringResource(id = R.string.cashMoney)
                )

                Text(
                    text = stringResource(id = R.string.availableBalance),
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
                stringResource(id = R.string.bankAccs)
            )

            bankCount = bankAccountCount(cashDetailsList)

            Text(
                text = "$bankCount " + stringResource(id = R.string.accs)
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
                    text = stringResource(id = R.string.fixedDep),
                    fontFamily = poppinsFontFamily,
                    fontSize = 15.sp,
                    color = HeaderTitle
                )

                Text(
                    text = stringResource(id = R.string.text_5),
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
                    text = stringResource(id = R.string.text_6),
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
                stringResource(id = R.string.fdAcc)
            )

            fdCount = fdCount(fdAccDetailsList)

            Text(text = "$fdCount " + stringResource(id = R.string.accs))
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
                    text = stringResource(id = R.string.stock),
                    fontFamily = poppinsFontFamily,
                    fontSize = 15.sp,
                    color = HeaderTitle
                )

                Text(
                    text = stringResource(id = R.string.text_7),
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
                    text = stringResource(id = R.string.text_8),
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
                stringResource(id = R.string.stocks)
            )

            stockCount = stocksCount(stockListLive)

            Text(
                text = "$stockCount " + stringResource(id = R.string.stocks)
            )
        }

    }
}

fun bankAccountCount(cashList: List<Cash>): Int{
    var count = 0

    for (cashAccount in cashList) {
        if(cashAccount.typeName != "Cash")
            count++
    }

    return count
}

fun stocksCount(stockList: List<Stock>): Int{
    var count = 0

    for (stockDetails in stockList)
        count++

    return count
}

fun fdCount(fdAccDetailsList: List<FDAccount>): Int{
    var accountCount = 0

    for (fdAccount in fdAccDetailsList) {
        accountCount++
    }
    return accountCount
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
