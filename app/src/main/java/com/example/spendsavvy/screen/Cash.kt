package com.example.spendsavvy.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.spendsavvy.components.bounceClick
import com.example.spendsavvy.models.Cash
import com.example.spendsavvy.navigation.Screen
import com.example.spendsavvy.ui.theme.poppinsFontFamily
import com.example.spendsavvy.viewModels.WalletViewModel
import java.text.NumberFormat

@Composable
fun CashScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    cashViewModel: WalletViewModel
) {
    var cashAmount by remember {
        mutableStateOf(0.00)
    }

    var count by remember {
        mutableStateOf(0)
    }

    val totalAccount = remember {
        mutableStateOf(0)
    }

    val cashDetailsList by cashViewModel.cashDetailsList.observeAsState(initial = emptyList())

    Column(modifier = Modifier.padding(10.dp)) {
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "Cash",
            fontFamily = poppinsFontFamily,
            fontSize = 25.sp
        )
        Text(
            text = "Includes cash money, bank accounts, and eWallet",
            fontSize = 10.sp,
            fontFamily = poppinsFontFamily,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Cash Money",
                    fontSize = 20.sp,
                    fontFamily = poppinsFontFamily
                )

                Text(
                    text = "Available balance",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    fontFamily = poppinsFontFamily
                )

            }

            /*for (cashDetails in cashDetailsList) {
                if (cashDetails.typeName == "Cash") {
                    count = 1
                    cashAmount = cashDetails.balance
                } else
                    totalAccount.value++
            }*/
            totalAccount.value = accountCount(cashList = cashDetailsList)

            for (cashDetails in cashDetailsList) {
                if (cashDetails.typeName == "Cash") {
                    count = 1
                    cashAmount = cashDetails.balance
                }
            }

            if (count == 1)
                Text(
                    text = "RM $cashAmount",
                    fontSize = 20.sp,
                    fontFamily = poppinsFontFamily
                )
            else
                Text(
                    text = "RM 0.00",
                    fontSize = 20.sp,
                    fontFamily = poppinsFontFamily
                )

        }

        Spacer(modifier = Modifier.height(15.dp))
        Divider(color = Color.Gray, thickness = 0.7.dp)
        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Bank Accounts",
                fontSize = 20.sp,
                fontFamily = poppinsFontFamily
            )

            Text(
                text = "${totalAccount.value} Accounts",    //display total bank accounts
                fontFamily = poppinsFontFamily
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        Divider(color = Color.Gray, thickness = 0.7.dp)

        BankAccList(cashDetailsList, modifier)

        Spacer(modifier = Modifier.height(15.dp))
        Divider(color = Color.Gray, thickness = 0.7.dp)
        Spacer(modifier = Modifier.height(15.dp))

        Box(modifier = Modifier.fillMaxSize()) {
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = {
                                navController.navigate(route = Screen.AddCashAccount.route)
                            }
                        ) {
                            Text(
                                text = "Add Account",
                                textAlign = TextAlign.Center,
                                fontFamily = poppinsFontFamily,
                            )
                        }

                        Button(
                            onClick = {
                                navController.navigate(route = Screen.EditCashAccount.route)
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
                                text = "Edit Account",
                                textAlign = TextAlign.Center,
                                fontFamily = poppinsFontFamily,
                                color = Color.White
                            )
                        }


                    }
                }
            }
        }

    }


}

//do all are clickable later
@Composable
fun BankAccList(
    bankList: List<Cash>,
    modifier: Modifier
) {
    LazyColumn {
        items(bankList) { item: Cash ->
            if (item.typeName != "Cash") {
                Spacer(modifier = Modifier.height(15.dp))
                Divider(color = Color.Gray, thickness = 0.7.dp)
                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item.typeName,
                        fontFamily = poppinsFontFamily,
                        fontSize = 15.sp
                    )

                    Text(
                        text = "RM ${item.balance}",
                        fontFamily = poppinsFontFamily,
                        fontSize = 15.sp
                    )
                }

            }
        }
    }
}

@Composable
fun accountCount(cashList: List<Cash>): Int{
    var count = 0

    for (cashAccount in cashList) {
        if(cashAccount.typeName != "Cash")
            count++
    }

    return count
}

/*@Preview(showBackground = true)
@Composable
fun CashScreenPreview() {
    CashScreen(
        Modifier
            .fillMaxSize()
            .padding(20.dp),
        cash = Cash("Cash", "Cash", 200.0),
        cashViewModel = viewModel()
    )

}*/
