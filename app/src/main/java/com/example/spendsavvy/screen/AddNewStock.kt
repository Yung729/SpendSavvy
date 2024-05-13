package com.example.spendsavvy.screen

import android.annotation.SuppressLint
import android.net.Uri
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.Stock
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.navigation.Screen
import com.example.spendsavvy.ui.theme.poppinsFontFamily
import com.example.spendsavvy.viewModels.OverviewViewModel
import com.example.spendsavvy.viewModels.WalletViewModel
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState", "RememberReturnType")
@Composable
fun AddNewStockScreen(
    modifier: Modifier,
    walletViewModel: WalletViewModel,
    transactionViewModel: OverviewViewModel,
    navController: NavController
) {

    val stockLiveDataList by walletViewModel.stockListLive.observeAsState(initial = emptyList())

    var stockName by remember {
        mutableStateOf("")
    }

    var price by remember {
        mutableStateOf("")
    }

    var qty by remember {
        mutableStateOf("")
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, top = 15.dp)
    ) {
        Text(
            text = "Add New Product",
            fontFamily = poppinsFontFamily,
            fontSize = 25.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, top = 30.dp, bottom = 30.dp)
        ) {
            Text(
                text = "Product Name",
                fontFamily = poppinsFontFamily,
                fontSize = 15.sp
            )

            TextField(
                value = stockName,
                onValueChange = {
                    stockName = it
                },
                placeholder = {
                    Text(
                        text = "Product",
                        fontFamily = poppinsFontFamily,
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                }
            )

            Text(
                text = "Product Price",
                fontFamily = poppinsFontFamily,
                fontSize = 15.sp
            )

            TextField(
                value = price,
                onValueChange = {
                    price = it
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

            Text(
                text = "Quantity",
                fontFamily = poppinsFontFamily,
                fontSize = 15.sp
            )

            TextField(
                value = qty,
                onValueChange = {
                    qty = it
                },
                placeholder = {
                    Text(
                        text = "0",
                        fontFamily = poppinsFontFamily,
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                }
            )

            Spacer(modifier = Modifier.height(30.dp))
        }
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


                    transactionViewModel.addTransactionToFirestore(
                        Transactions(
                            id = transactionViewModel.generateTransactionId(),
                            amount = ((price.toDoubleOrNull() ?: 0.0 )* qty.toInt()) ,
                            description = "Purchase Stock",
                            date = Date(),
                            category = Category(
                                id = "CT0014",
                                imageUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/spendsavvy-5a2a8.appspot.com/o/images%2Fstock.png?alt=media&token=416dc2e0-caf2-4c9e-a664-2c0eceba49fb"),
                                categoryName = "Stock Purchase",
                                categoryType = "Expenses"
                            ),
                            paymentMethod = "Cash",
                            transactionType = "Expenses"
                        ),
                        onSuccess = {
                            walletViewModel.addStockDetailsToDatabase(
                                Stock(
                                    stockName,
                                    price.toDoubleOrNull() ?: 0.0,
                                    qty.toInt()
                                )
                            )
                            navController.navigateUp()
                        },
                        onFailure = {
                        }
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