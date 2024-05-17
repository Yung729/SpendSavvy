package com.example.spendsavvy.screen.wallet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import com.example.spendsavvy.R
import com.example.spendsavvy.components.bounceClick
import com.example.spendsavvy.models.FDAccount
import com.example.spendsavvy.navigation.Screen
import com.example.spendsavvy.ui.theme.poppinsFontFamily
import com.example.spendsavvy.viewModels.WalletViewModel

@Composable
fun FixedDepositScreen(
    modifier: Modifier,
    navController: NavController,
    walletViewModel: WalletViewModel
) {

    val fdAccDetailsList by walletViewModel.fdAccDetailsList.observeAsState(initial = emptyList())

    Column(
        modifier = Modifier.padding(15.dp)
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = stringResource(id = R.string.fixedDep),
            fontFamily = poppinsFontFamily,
            fontSize = 25.sp
        )
        Text(
            text = stringResource(id = R.string.text_5),
            fontSize = 10.sp,
            color = Color.Gray,
            fontFamily = poppinsFontFamily
        )

        val accountCount = fdCount(fdAccDetailsList = fdAccDetailsList)

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = stringResource(id = R.string.fixedDep),
                fontSize = 17.sp,
                fontFamily = poppinsFontFamily
            )

            Text(
                text = "$accountCount " + stringResource(id = R.string.accs),
                fontSize = 17.sp,
                fontFamily = poppinsFontFamily
            )
        }

        Spacer(modifier = Modifier.height(15.dp))
        Divider(color = Color.Gray, thickness = 0.7.dp)

        Box(modifier = Modifier.fillMaxSize()) {

            BankList(fdAccountList = fdAccDetailsList, navController = navController)

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
                        onClick = {
                            navController.navigate(route = Screen.FixedDepositDetails.route)
                        },
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .bounceClick()
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.addFDAcc),
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BankCard(fdAccount: FDAccount, modifier: Modifier, navController: NavController) {
    Card(
        modifier = modifier
            .clickable {
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    key = "currentFDAccount",
                    value = fdAccount
                )
                navController.navigate(Screen.FDEarnScreen.route)
            },
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

            /*Image(
                painter = painterResource(id = stock.productImage),
                contentDescription = "",
                modifier = Modifier
                    .size(30.dp, 30.dp)
                    .padding(end = 10.dp)
            )*/

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
            ) {
                Text(
                    text = fdAccount.bankName,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "RM ${fdAccount.deposit}",
                    fontFamily = poppinsFontFamily,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}

@Composable
fun BankList(
    fdAccountList: List<FDAccount>,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    LazyColumn(modifier = modifier) {
        items(fdAccountList) { bankAccount ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BankCard(
                    fdAccount = bankAccount,
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    navController = navController
                )
            }

        }
    }
}

