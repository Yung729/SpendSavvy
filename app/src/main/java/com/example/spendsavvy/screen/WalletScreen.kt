package com.example.spendsavvy.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.spendsavvy.R
import com.example.spendsavvy.components.bounceClick
import com.example.spendsavvy.models.Cash
import com.example.spendsavvy.models.FDAccount
import com.example.spendsavvy.models.Stock
import com.example.spendsavvy.navigation.Screen
import com.example.spendsavvy.ui.theme.GreenColor
import com.example.spendsavvy.ui.theme.HeaderTitle
import com.example.spendsavvy.ui.theme.poppinsFontFamily
import com.example.spendsavvy.viewModels.WalletViewModel
import java.util.Calendar

@Composable
fun WalletScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    walletViewModel: WalletViewModel
) {
    var isSelectionPopUp by remember {
        mutableStateOf(false)
    }

    var count by remember {
        mutableIntStateOf(0)
    }
    var bankCount by remember {
        mutableIntStateOf(0)
    }
    var fdCount by remember {
        mutableIntStateOf(0)
    }
    var stockCount by remember {
        mutableIntStateOf(0)
    }

    val cashDetailsList by walletViewModel.cashDetailsList.observeAsState(initial = emptyList())
    val fdAccDetailsList by walletViewModel.fdAccDetailsList.observeAsState(initial = emptyList())
    val stockListLive by walletViewModel.stockListLive.observeAsState(initial = emptyList())

    val totalCashAmount by walletViewModel.totalCashAmount.observeAsState(initial = 0.0)
    val totalFixedDeposit by walletViewModel.totalFixedDeposit.observeAsState(initial = 0.0)
    val totalPriceStock by walletViewModel.totalPriceStock.observeAsState(initial = 0.0)

    Column(
        modifier = modifier
    ) {

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
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF696161), // Start color
                                Color(0xFF1B1B1B)  // End color
                            )
                        )
                    )
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(15.dp)) {
                    Text(
                        stringResource(id = R.string.availableBalance),
                        fontSize = 20.sp
                    )

                    Text(
                        text = "RM ${totalCashAmount + totalFixedDeposit + totalPriceStock}",
                        fontSize = 20.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

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
                border = BorderStroke(1.dp, Color.Black),
                modifier = Modifier.size(100.dp, 40.dp)

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

            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.money),
                    contentDescription = "",
                    modifier = Modifier.size(20.dp, 20.dp)
                )


                Column {
                    Text(
                        stringResource(id = R.string.cashMoney), textAlign = TextAlign.Left
                    )

                    Text(
                        text = stringResource(id = R.string.availableBalance),
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
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

            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bank),
                    contentDescription = "",
                    modifier = Modifier.size(20.dp, 20.dp)
                )

                Text(
                    stringResource(id = R.string.bankAccs)
                )
            }

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
                border = BorderStroke(1.dp, Color.Black),
                modifier = Modifier.size(100.dp, 40.dp)

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

            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.deposit),
                    contentDescription = "",
                    modifier = Modifier.size(20.dp, 20.dp)
                )

                Text(
                    stringResource(id = R.string.fdAcc)
                )
            }

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
                border = BorderStroke(1.dp, Color.Black),
                modifier = Modifier.size(100.dp, 40.dp)

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

            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.stock),
                    contentDescription = "",
                    modifier = Modifier.size(20.dp, 20.dp)
                )

                Text(
                    stringResource(id = R.string.stocks)
                )
            }

            stockCount = stocksCount(stockListLive)

            Text(
                text = "$stockCount " + stringResource(id = R.string.stocks)
            )
        }


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
                    Button(
                        onClick = {
                            isSelectionPopUp = true
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
                            text = "Transfer",
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }

    if (isSelectionPopUp)
        TransferDialog(onCancelClick = { isSelectionPopUp = false }, {}, walletViewModel)
}

@Composable
fun TransferDialog(
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
    walletViewModel: WalletViewModel
) {
    var isClicked by remember {
        mutableIntStateOf(0)
    }

    var isSelectionClicked by remember {
        mutableStateOf(false)
    }

    Dialog(
        onDismissRequest = { onCancelClick() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, color = Color.Gray, shape = RoundedCornerShape(15.dp))
                .shadow(elevation = 15.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {

                Button(
                    onClick = {
                        isClicked = 1
                        isSelectionClicked = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                ) {
                    Text(
                        text = "Between Cash Accounts",
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFontFamily
                    )
                }

                Button(
                    onClick = {
                        isClicked = 2
                        isSelectionClicked = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                ) {
                    Text(
                        text = "Cash Account To FD Account",
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFontFamily
                    )
                }
            }
        }
    }

    if (isClicked == 1 && isSelectionClicked)
        TransferAccountDialog(
            onCancelClick = { isSelectionClicked = false },
            {},
            walletViewModel,
            1
        )
    else if (isClicked == 2 && isSelectionClicked)
        TransferAccountDialog(
            onCancelClick = { isSelectionClicked = false },
            {},
            walletViewModel,
            2
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferAccountDialog(
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
    walletViewModel: WalletViewModel,
    mode: Int
) {
    val context = LocalContext.current
    val cashDetailsList by walletViewModel.cashDetailsList.observeAsState(initial = emptyList())

    val currentDate = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

    var rate by remember {
        mutableStateOf("")
    }

    var balanceFound by remember {
        mutableDoubleStateOf(0.0)
    }

    var newFD by remember {
        mutableStateOf("")
    }

    var searchAccount1 by remember {
        mutableStateOf("")
    }

    var searchAccount2 by remember {
        mutableStateOf("")
    }

    var isExpanded by remember {
        mutableStateOf(false)
    }

    var transferAmt by remember {
        mutableStateOf("")
    }

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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, top = 15.dp)
            ) {
                Row {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "From",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Text("Cash Account")

                Spacer(modifier = Modifier.height(25.dp))

                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = { isExpanded = it }
                ) {
                    TextField(
                        value = searchAccount1,
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
                            value = searchAccount1,
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
                            for (cash in cashDetailsList) {                 //read from existing stock items
                                if (searchAccount2 != cash.typeName)
                                    DropdownMenuItem(
                                        text = {
                                            Text(text = cash.typeName)
                                        },
                                        onClick = {
                                            searchAccount1 = cash.typeName
                                            isExpanded = false
                                        }
                                    )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))

                Text("Available Balance: ")

                Spacer(modifier = Modifier.height(15.dp))

                for (cashDetails in cashDetailsList) {
                    if (cashDetails.typeName == searchAccount1) {
                        Text("RM ${cashDetails.balance}")
                        balanceFound = cashDetails.balance
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Text("Transfer amount")

                Spacer(modifier = Modifier.height(15.dp))

                TextField(
                    value = transferAmt,
                    onValueChange = {
                        transferAmt = it
                    },
                    placeholder = {
                        Text(
                            text = "RM 0.00",
                            fontFamily = poppinsFontFamily,
                            fontSize = 15.sp,
                            color = Color.Gray
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Decimal
                    )
                )

                Spacer(modifier = Modifier.height(30.dp))

                Row {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "To",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                if (mode == 1)
                    Text("Cash Account")
                else
                    Text("FD Account")

                Spacer(modifier = Modifier.height(25.dp))

                if (mode == 1) {
                    ExposedDropdownMenuBox(
                        expanded = isExpanded,
                        onExpandedChange = { isExpanded = it }
                    ) {
                        TextField(
                            value = searchAccount2,
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
                                value = searchAccount2,
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
                                for (cash in cashDetailsList) {
                                    if (searchAccount1 != cash.typeName) {
                                        DropdownMenuItem(
                                            text = {
                                                Text(text = cash.typeName)
                                            },
                                            onClick = {
                                                searchAccount2 = cash.typeName
                                                isExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    TextField(
                        value = newFD,
                        onValueChange = {
                            newFD = it
                        },
                        placeholder = {
                            Text(
                                text = "Public bank",
                                fontFamily = poppinsFontFamily,
                                fontSize = 15.sp,
                                color = Color.Gray
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Text
                        )
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    TextField(
                        value = rate,
                        onValueChange = {
                            rate = it
                        },
                        placeholder = {
                            Text(
                                text = "0.00%",
                                fontFamily = poppinsFontFamily,
                                fontSize = 15.sp,
                                color = Color.Gray
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Decimal
                        )
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                        if ((transferAmt.toDoubleOrNull() ?: 0.0) > balanceFound) {
                            Toast.makeText(
                                context,
                                "Please enter the amount that less than or equals to your deposit amount\nAccount Balance: ${balanceFound}",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            if (mode == 1) {
                                for (cashDetails in cashDetailsList) {
                                    if (cashDetails.typeName == searchAccount1) {
                                        walletViewModel.editCashDetails(
                                            cash = cashDetails,
                                            updatedCashDetails = Cash(
                                                imageUri = cashDetails.imageUri,
                                                typeName = cashDetails.typeName,
                                                balance = (cashDetails.balance - (transferAmt.toDoubleOrNull()
                                                    ?: 0.0))
                                            )
                                        )
                                    }

                                    if (cashDetails.typeName == searchAccount2) {
                                        walletViewModel.editCashDetails(
                                            cash = cashDetails,
                                            updatedCashDetails = Cash(
                                                imageUri = cashDetails.imageUri,
                                                typeName = cashDetails.typeName,
                                                balance = (cashDetails.balance + (transferAmt.toDoubleOrNull()
                                                    ?: 0.0))
                                            )
                                        )
                                    }
                                }
                            } else {
                                for (cashDetails in cashDetailsList) {
                                    if (cashDetails.typeName == searchAccount1) {
                                        walletViewModel.editCashDetails(
                                            cash = cashDetails,
                                            updatedCashDetails = Cash(
                                                imageUri = cashDetails.imageUri,
                                                typeName = cashDetails.typeName,
                                                balance = (cashDetails.balance - (transferAmt.toDoubleOrNull()
                                                    ?: 0.0))
                                            )
                                        )

                                        walletViewModel.addFDDetailsToDatabase(
                                            fdAccount = FDAccount(
                                                imageUri = cashDetails.imageUri,
                                                bankName = newFD,
                                                interestRate = rate.toDoubleOrNull() ?: 0.0,
                                                deposit = transferAmt.toDoubleOrNull() ?: 0.0,
                                                date = currentDate,
                                                transferType = "Deposit"
                                            ), cashDetails.imageUri

                                        )
                                    }
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Transfer",
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFontFamily
                    )
                }
            }
        }
    }
}

fun bankAccountCount(cashList: List<Cash>): Int {
    var count = 0

    for (cashAccount in cashList) {
        if (cashAccount.typeName != "Cash")
            count++
    }

    return count
}

fun stocksCount(stockList: List<Stock>): Int {
    var count = 0

    for (stockDetails in stockList)
        count++

    return count
}

fun fdCount(fdAccDetailsList: List<FDAccount>): Int {
    var accountCount = 0

    for (fdAccount in fdAccDetailsList) {
        accountCount++
    }
    return accountCount
}

