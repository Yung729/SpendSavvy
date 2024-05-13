package com.example.spendsavvy.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spendsavvy.models.FDAccount
import com.example.spendsavvy.navigation.Screen
import com.example.spendsavvy.ui.theme.poppinsFontFamily
import com.example.spendsavvy.viewModels.WalletViewModel
import kotlin.time.toDuration


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState", "RememberReturnType")
@Composable
fun FixedDepositDetailsScreen(
    modifier: Modifier,
    walletViewModel: WalletViewModel,
    navController: NavController
) {
    val cashDetailsList by walletViewModel.cashDetailsList.observeAsState(initial = emptyList())

    /*var isExpanded by remember {
        mutableStateOf(false)
    }*/

    var selectedBank by remember {
        mutableStateOf("")
    }

    var depositAmt by remember {
        mutableStateOf("")
    }

    var interestRate by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 15.dp, top = 15.dp
            )) {
        Text(
            text = "Fixed Deposit",
            fontFamily = poppinsFontFamily,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(15.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 15.dp)
        ) {
            Text(
                text = "Note: The amount of money will be locked for 1 year",
                fontFamily = poppinsFontFamily,
                fontSize = 15.sp,
                color = Color.Red
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Select your bank",
                fontFamily = poppinsFontFamily,
                fontSize = 15.sp
            )

            ClickableText(
                text = AnnotatedString("Click me to add more bank"),
                onClick = {
                          navController.navigate(Screen.AddCashAccount.route)
                }
            )

            /*ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it }
            ) {
                TextField(
                    value = searchBank,
                    onValueChange = {
                        searchBank = it
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
                    for (bankAccount in cashDetailsList) {
                        DropdownMenuItem(
                            text = {
                                Text(text = bankAccount.typeName)
                            },
                            onClick = {
                                searchBank = bankAccount.typeName
                                isExpanded = false
                            }
                        )
                    }
                }
            }*/
            TextField(
                value = selectedBank,
                onValueChange = {
                    selectedBank = it
                },
                placeholder = {
                    Text(
                        text = "Public bank",
                        fontFamily = poppinsFontFamily,
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                }
            )

            Spacer(modifier = Modifier.height(15.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 15.dp)
        ) {

            Text(
                text = "Deposit Amount",
                fontFamily = poppinsFontFamily,
                fontSize = 15.sp
            )

            TextField(
                value = depositAmt,
                onValueChange = {
                    depositAmt = it
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

            Text(
                text = "Interest Rate (%)",
                fontFamily = poppinsFontFamily,
                fontSize = 15.sp
            )

            TextField(
                value = interestRate,
                onValueChange = {
                    interestRate = it
                },
                placeholder = {
                    Text(
                        text = "0.00",
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
                .padding(15.dp),
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
                    text = "Cancel",
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFontFamily
                )
            }

            Button(
                onClick = {
                    //addFDDetailsIntoDatabase(FDAccount(searchName, duration, interestRate, depositAmt))
                    walletViewModel.addFDDetailsToDatabase(
                        FDAccount(
                            selectedBank,
                            interestRate.toDoubleOrNull() ?: 0.0,
                            depositAmt.toDoubleOrNull() ?: 0.0
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = "Add",
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFontFamily
                )
            }
        }
    }
}
