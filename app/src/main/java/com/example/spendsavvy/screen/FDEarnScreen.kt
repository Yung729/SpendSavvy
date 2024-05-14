package com.example.spendsavvy.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.spendsavvy.components.bounceClick
import com.example.spendsavvy.models.Cash
import com.example.spendsavvy.models.FDAccount
import com.example.spendsavvy.ui.theme.poppinsFontFamily
import com.example.spendsavvy.viewModels.WalletViewModel


@Composable
fun FDEarnScreen(
    modifier: Modifier,
    navController: NavController,
    fdAccount: FDAccount,
    walletViewModel: WalletViewModel
) {

    var isPopUp by remember {
        mutableStateOf(false)
    }

    val daysLeft = walletViewModel.convertDateIntoMillisecond(fdAccount.date.toString())

    Column(
        modifier = Modifier.padding(start = 15.dp)
    ) {

        Text(
            "${fdAccount.bankName} - FD",
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

                    //image

                    Text(text = "RM ${fdAccount.deposit}")

                    Text(
                        "Account Balance",
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                border = BorderStroke(2.dp, Color.Black),
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                Column {

                    Text(
                        "Interest Earned",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            start = 10.dp,
                            top = 10.dp,
                            end = 10.dp,
                            bottom = 3.dp
                        )
                    )

                    Text(
                        "RM ${fdAccount.deposit * fdAccount.interestRate / 100}",    //temporary
                        color = Color.Green,
                        modifier = Modifier.padding(
                            start = 10.dp,
                            top = 3.dp,
                            end = 10.dp,
                            bottom = 10.dp
                        )
                    )

                }

            }

            Card(
                border = BorderStroke(2.dp, Color.Black),
                modifier = Modifier
                    .padding(start = 50.dp)
            ) {
                Column {

                    Text(
                        "Days Left",
                        fontSize = 15.sp,
                        modifier = Modifier.padding(
                            start = 10.dp,
                            top = 10.dp,
                            end = 10.dp,
                            bottom = 3.dp
                        )
                    )

                    //show how many days left
                    Text(
                        "$daysLeft Days",
                        color = Color.Gray,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(
                            start = 10.dp,
                            top = 3.dp,
                            end = 10.dp,
                            bottom = 10.dp
                        )
                    )

                    Text(
                        "Interest Rate",
                        fontSize = 15.sp,
                        modifier = Modifier.padding(
                            start = 10.dp,
                            top = 3.dp,
                            end = 10.dp,
                            bottom = 3.dp
                        )
                    )

                    //show how many days left
                    Text(
                        "${fdAccount.interestRate} %",
                        fontSize = 15.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(
                            start = 10.dp,
                            top = 3.dp,
                            end = 10.dp,
                            bottom = 10.dp
                        )
                    )
                }

            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "Record",
            fontSize = 20.sp
        )

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            //FDTransferList()
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
                            isPopUp = true
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
                            text = "Withdraw All",
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            navController.navigateUp()
                        },
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .bounceClick()
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Cancel",
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
    if (isPopUp)
        WithdrawFDScreen(onCancelClick = { isPopUp = false }, {}, walletViewModel, daysLeft, (fdAccount.deposit + (fdAccount.deposit * fdAccount.interestRate / 100)))

}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState", "RememberReturnType")
@Composable
fun WithdrawFDScreen(
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
    walletViewModel: WalletViewModel,
    daysLeft: Long,
    totalEarned: Double
) {
    val cashDetailsList by walletViewModel.cashDetailsList.observeAsState(initial = emptyList())

    var searchAccount by remember {
        mutableStateOf("")
    }

    var isExpanded by remember {
        mutableStateOf(false)
    }


    var withdrawalAmt by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    Dialog(
        onDismissRequest = { onCancelClick() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .border(1.dp, color = Color.Gray, shape = RoundedCornerShape(15.dp))
                .shadow(elevation = 15.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                Text(
                    "Transfer",
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                TextField(
                    value = withdrawalAmt,
                    onValueChange = {
                        withdrawalAmt = it
                    },
                    placeholder = {
                        Text(
                            text = "RM 0.00",
                            fontFamily = poppinsFontFamily,
                            fontSize = 15.sp,
                            color = Color.Gray
                        )
                    }
                )

                Spacer(modifier = Modifier.height(15.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "To",
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    "Cash Account"
                )

                Spacer(modifier = Modifier.height(10.dp))

                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = { isExpanded = it }
                ) {
                    TextField(
                        value = searchAccount,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenuBox(
                        expanded = isExpanded,
                        onExpandedChange = { isExpanded = it }
                    ) {
                        TextField(
                            value = searchAccount,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                            },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = isExpanded,
                            onDismissRequest = { isExpanded = false }
                        ) {
                            for (cash in cashDetailsList) {          //read from existing stock items
                                DropdownMenuItem(
                                    text = {
                                        Text(text = cash.typeName)
                                    },
                                    onClick = {
                                        searchAccount = cash.typeName
                                        isExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                OutlinedButton(
                    onClick = {
                        if (daysLeft == 0.toLong()) {
                            for(cashDetails in cashDetailsList){
                                if(cashDetails.typeName == searchAccount)
                                    walletViewModel.editCashDetails(
                                        cash = cashDetails,
                                        updatedCashDetails = Cash(
                                            cashDetails.typeName,
                                            cashDetails.balance + totalEarned
                                        )
                                    )
                            }

                        } else {
                            Toast.makeText(
                                context,
                                "The FD Account is still locked\nRemaining days: $daysLeft",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        //walletViewModel.editCashDetails()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(
                        text = "Withdraw FD Money",
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFontFamily
                    )
                }
            }

        }
    }
}

/*
@Composable
fun FDTransferList(

) {
    LazyColumn {
items() { item: Cash ->
            if (item.typeName != "Cash") {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item.,
                        fontFamily = poppinsFontFamily,
                        fontSize = 15.sp
                    )

                    Text(
                        text = "RM ${item.}",
                        fontFamily = poppinsFontFamily,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))
                Divider(color = Color.Gray, thickness = 0.7.dp)
                Spacer(modifier = Modifier.height(15.dp))
            }
        }

    }
}
*/
