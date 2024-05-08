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
import com.example.spendsavvy.components.bounceClick
import com.example.spendsavvy.models.Cash
import com.example.spendsavvy.ui.theme.poppinsFontFamily
import com.example.spendsavvy.viewModels.WalletViewModel
import java.text.NumberFormat

@Composable
fun CashScreen(
    modifier: Modifier = Modifier,
    cashViewModel: WalletViewModel
) {

    var isDialogPopUp = remember { mutableStateOf(false) }

    var totalAccount = remember {
        mutableStateOf(0)
    }


    val cashDetailsList by cashViewModel.cashDetailsList.observeAsState(initial = emptyList())

    Column(modifier = Modifier.padding(10.dp)) {
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "Cash",
            fontFamily = poppinsFontFamily,
            fontSize = 15.sp
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
                    fontFamily = poppinsFontFamily
                )

                Text(
                    text = "Available balance",
                    color = Color.Gray,
                    fontFamily = poppinsFontFamily
                )

            }

            for (cashDetails in cashDetailsList) {
                if (cashDetails.type == "Cash")
                    CashList(cash = cashDetails)
                else
                    totalAccount.value++
            }
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
                text = "Fixed Deposit Accounts",
                fontFamily = poppinsFontFamily
            )

            Text(
                text = "${totalAccount.value} Accounts", /*calculate acc*/
                fontFamily = poppinsFontFamily
            )
        }

        for (cashDetails in cashDetailsList) {
            if (cashDetails.type == "Bank")
                BankAccList(cashDetailsList, modifier)

            Spacer(modifier = Modifier.height(15.dp))
            Divider(color = Color.Gray, thickness = 0.7.dp)
            Spacer(modifier = Modifier.height(15.dp))
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

            }

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
                        onClick = { isDialogPopUp.value = true },
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

        if (isDialogPopUp.value)
            CashPopUpScreen(
                onCancelClick = { isDialogPopUp.value = false },
                {},
                cashViewModel
            )
    }


}

//change to new UI screen
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState", "RememberReturnType")
@Composable
fun CashPopUpScreen(
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
    walletViewModel: WalletViewModel
) {


    var bankName by remember {
        mutableStateOf("Public Bank")
    }

    var incAmt by remember {
        mutableStateOf("")
    }

    var decAmt by remember {
        mutableStateOf("")
    }

    var selectedIndex by remember {
        mutableStateOf(0)
    }

    val options = mutableStateListOf<String>("Cash", "Bank")
    val cashInfo by walletViewModel.cashDetailsList.observeAsState(initial = emptyList())
    //var cash
    val addition = incAmt.toDoubleOrNull() ?: 0.0
    val substraction = decAmt.toDoubleOrNull() ?: 0.0

    //initial amount column will not show if the type already existed

    /*   val totalBalance =
           calculateCashBalance(balance = cashInfo[0].balance, incAmt = addition, decAmt = substraction)
   */ //calculation


    /*for (account in cashInfo){
        if (account.type =="Cash"){
            cash = account
        }
    }*/

    Dialog(
        onDismissRequest = { onCancelClick() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .border(1.dp, color = Color.Gray, shape = RoundedCornerShape(15.dp))
                .shadow(elevation = 15.dp)
        ) {
            Text(
                text = "Account",
                fontFamily = poppinsFontFamily,
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(start = 15.dp, top = 15.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                SingleChoiceSegmentedButtonRow {
                    options.forEachIndexed { index, option ->
                        SegmentedButton(
                            selected = selectedIndex == index,
                            onClick = { selectedIndex = index },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index, count = options.size
                            )
                        ) {
                            Text(text = option)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            if (selectedIndex == 1) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, top = 15.dp)
                ) {

                    Text(
                        text = "Enter your bank",
                        fontFamily = poppinsFontFamily,
                        fontSize = 15.sp
                    )

                    TextField(
                        value = bankName,
                        onValueChange = {
                            bankName = it
                        },
                        placeholder = {
                            Text(
                                text = "Public Bank",
                                fontFamily = poppinsFontFamily,
                                fontSize = 15.sp,
                                color = Color.Gray
                            )
                        }
                    )

                    //val type = "bank"

                    /*ExposedDropdownMenuBox(
                        expanded = isExpanded,
                        onExpandedChange = { isExpanded = it }
                    ) {
                        TextField(
                            value = searchBank,
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
                            for (i in bankName) {
                                DropdownMenuItem(
                                    text = {
                                        Text(text = i)
                                    },
                                    onClick = {
                                        searchBank = i
                                        isExpanded = false
                                    }
                                )
                            }
                        }
                    }*/
                }
                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "Increase Amount",
                    fontFamily = poppinsFontFamily,
                    fontSize = 15.sp
                )

                TextField(
                    value = incAmt,
                    onValueChange = {
                        incAmt = it
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

                Spacer(modifier = Modifier.height(30.dp))
                //if data exists, only show this feature

                for (cash in cashInfo) {
                    if (bankName == cash.typeName) {
                        Text(
                            text = "Decrease Amount",
                            fontFamily = poppinsFontFamily,
                            fontSize = 15.sp
                        )

                        TextField(
                            value = decAmt,
                            onValueChange = {
                                decAmt = it
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
                    } else {
                        //addCashDetailsToDataBase
                    }
                }

            }


            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier
                    .padding(15.dp),
                horizontalArrangement = Arrangement.spacedBy(30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        onCancelClick()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(
                        text = "Cancel",
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFontFamily
                    )
                }

                Button(
                    onClick = {
                        for (cash in cashInfo) {
                            if (bankName == cash.typeName) {

                                walletViewModel.editCashDetails(
                                    cash = cash,
                                    updatedCashDetails = Cash(
                                        type = cash.type,
                                        typeName = cash.typeName,
                                        balance = cash.balance + incAmt.toDoubleOrNull() as Double - decAmt.toDoubleOrNull() as Double
                                    )
                                )
                            }
                            else
                                walletViewModel.addCashDetailsToDatabase(
                                    Cash(
                                        cash.type,
                                        cash.typeName,
                                        cash.balance
                                    )
                                )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(
                        text = "Update",
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFontFamily
                    )
                }
            }
        }
    }
}

@Composable
fun CashList(           //used to return cash balance only
    cash: Cash
) {
    Text(
        text = "RM ${cash.balance}",
        fontFamily = poppinsFontFamily
    )
}

@Composable
fun BankAccList(
    bankList: List<Cash>,
    modifier: Modifier
) {
    LazyColumn {
        items(bankList) { item: Cash ->
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = item.typeName)

                Text(text = "RM ${item.balance}")
            }
        }
    }
}

@Composable
fun calculateCashBalance(balance: Double, incAmt: Double, decAmt: Double): Double {
    return balance + incAmt - decAmt
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
