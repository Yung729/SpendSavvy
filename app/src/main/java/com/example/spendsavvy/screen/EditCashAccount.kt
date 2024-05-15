package com.example.spendsavvy.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spendsavvy.R
import com.example.spendsavvy.models.Cash
import com.example.spendsavvy.ui.theme.poppinsFontFamily
import com.example.spendsavvy.viewModels.WalletViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState", "RememberReturnType")
@Composable
fun EditCashAccountScreen(
    walletViewModel: WalletViewModel,
    navController: NavController
) {

    val context = LocalContext.current

    var isExpanded by remember {
        mutableStateOf(false)
    }

    var searchCashAccount by remember {
        mutableStateOf("")
    }

    var typeName by remember {
        mutableStateOf("")
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

    Column(
        modifier = Modifier
            .fillMaxWidth(0.85f)
    ) {
        Text(
            text = stringResource(id = R.string.acc),
            fontFamily = poppinsFontFamily,
            fontSize = 30.sp,
            modifier = Modifier
                .padding(start = 15.dp, top = 15.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Column(
            modifier = Modifier.padding(start = 30.dp, top = 15.dp, bottom = 30.dp)
        ) {
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
                Text(
                    text = stringResource(id = R.string.text_11),
                    fontFamily = poppinsFontFamily,
                    fontSize = 15.sp
                )

                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = { isExpanded = it }
                ) {
                    TextField(
                        value = searchCashAccount,
                        onValueChange = {
                            searchCashAccount = it
                        },
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
                        for (cashDetails in cashInfo) {          //read from existing stock items
                            if (cashDetails.typeName != "Cash") {
                                DropdownMenuItem(
                                    text = {
                                        Text(text = cashDetails.typeName)
                                    },
                                    onClick = {
                                        searchCashAccount = cashDetails.typeName
                                        isExpanded = false
                                    }
                                )
                                typeName = searchCashAccount
                            }
                        }
                    }
                }
            } else {
                typeName = "Cash"
            }

            Spacer(modifier = Modifier.height(30.dp))


            Text(
                text = stringResource(id = R.string.increaseAmount),
                fontFamily = poppinsFontFamily,
                fontSize = 15.sp
            )

            TextField(
                value = incAmt,
                onValueChange = {
                    incAmt = it
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Decimal
                ),
                placeholder = {
                    Text(
                        text = "RM 0.00",
                        fontFamily = poppinsFontFamily,
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                }
            )

            Text(
                text = stringResource(id = R.string.decreaseAmount),
                fontFamily = poppinsFontFamily,
                fontSize = 15.sp
            )

            TextField(
                value = decAmt,
                onValueChange = {
                    decAmt = it
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Decimal
                ),
                placeholder = {
                    Text(
                        text = "RM 0.00",
                        fontFamily = poppinsFontFamily,
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier
                .padding(30.dp),
            horizontalArrangement = Arrangement.spacedBy(30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    navController.navigateUp()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFontFamily
                )
            }

            Button(
                onClick = {
                    for (cashDetails in cashInfo) {
                        if (cashDetails.typeName == typeName) {
                            if ((decAmt.toDoubleOrNull() ?: 0.0) > (incAmt.toDoubleOrNull()
                                    ?: 0.0) + cashDetails.balance
                            ) {
                                Toast.makeText(
                                    context,
                                    "You cannot decrease your amount more than the total of your remaining and increased amount\nRemaining Amount: RM ${cashDetails.balance}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                walletViewModel.editCashDetails(
                                    cash = cashDetails,
                                    updatedCashDetails = Cash(
                                        imageUri = cashDetails.imageUri,
                                        typeName = typeName,
                                        balance = cashDetails.balance + (incAmt.toDoubleOrNull()
                                            ?: 0.0) - (decAmt.toDoubleOrNull() ?: 0.0)
                                    )
                                )
                            }
                        }
                    }
                    navController.navigateUp()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.update),
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFontFamily
                )
            }
        }
    }
}


/*
@Composable
fun cashUpdateDetails(cashList: List<Cash>, typeName: String): Cash {

    for (cashDetails in cashList) {
        if (cashDetails.typeName == typeName) {
            return cashDetails
        }
    }

}*/
