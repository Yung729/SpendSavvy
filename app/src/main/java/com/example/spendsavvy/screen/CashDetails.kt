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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.spendsavvy.models.Cash
import com.example.spendsavvy.ui.theme.poppinsFontFamily
import com.example.spendsavvy.viewModels.WalletViewModel

//DRAFT ONLY
@Composable
fun CashDetailsScreen(

) {
    //account

    //upload image(Cash no need, use money icon)


    //2 options (Cash or Bank)


    //if typeName found, let show initial amount textfield, else show increase amount & decrease amount (cash no need name {typeName = "Cash"}, bank need to specify bankName where typeName = bankName

    //button
    //condition: new account, use addDetailsToDatabase, else editDetailsToDatabase

}
//DRAFT ONLY

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState", "RememberReturnType")
@Composable
fun CashDetailsScreen(
    walletViewModel: WalletViewModel,
    navController: NavController
) {
    var existingAccount by remember {
        mutableStateOf(false)
    }

    var type by remember {
        mutableStateOf("Cash")
    }

    var typeName by remember {
        mutableStateOf("Public Bank")
    }

    var incAmt by remember {
        mutableStateOf("")
    }

    var decAmt by remember {
        mutableStateOf("")
    }

    var initialAmt by remember {
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
            text = "Account",
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
                type = "Bank"

                Text(
                    text = "Enter your bank",
                    fontFamily = poppinsFontFamily,
                    fontSize = 15.sp
                )

                TextField(
                    value = typeName,
                    onValueChange = {
                        typeName = it
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

            }

            Spacer(modifier = Modifier.height(30.dp))

            if (cashInfo.any { it.typeName == typeName }) {

                existingAccount = true
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
            }


            if (!existingAccount) {
                Text(
                    text = "Initial Amount",
                    fontFamily = poppinsFontFamily,
                    fontSize = 15.sp
                )

                TextField(
                    value = initialAmt,
                    onValueChange = {
                        initialAmt = it
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
                    if (cashInfo.any { it.typeName == typeName }) {

                        walletViewModel.editCashDetails(
                            cash = cashInfo.find { it.typeName == typeName }!!,
                            updatedCashDetails = Cash(
                                type = type,
                                typeName = typeName,
                                balance = initialAmt.toDoubleOrNull() as Double + incAmt.toDoubleOrNull() as Double - decAmt.toDoubleOrNull() as Double
                            )
                        )
                    } else
                        walletViewModel.addCashDetailsToDatabase(
                            Cash(
                                type = type,
                                typeName = typeName,
                                balance = initialAmt.toDoubleOrNull() ?: 0.0
                            )
                        )


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
                    text = "Update",
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFontFamily
                )
            }
        }
    }

}