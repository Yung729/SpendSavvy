package com.example.spendsavvy.screen

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.Stock
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.ui.theme.poppinsFontFamily
import com.example.spendsavvy.viewModels.OverviewViewModel
import com.example.spendsavvy.viewModels.WalletViewModel
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState", "RememberReturnType")
@Composable
fun EditExistingStockScreen(
    walletViewModel: WalletViewModel,
    transactionViewModel: OverviewViewModel,
    navController: NavController,
    mode: Int
) {
    val context = LocalContext.current

    val stockDetails by walletViewModel.stockListLive.observeAsState(initial = emptyList())

    var isExpanded by remember {
        mutableStateOf(false)
    }

    var searchProduct by remember {
        mutableStateOf("")
    }

    var price by remember {
        mutableStateOf("")
    }

    var qty by remember {
        mutableStateOf("")
    }

    var searchAccount by remember {
        mutableStateOf("")
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, top = 15.dp)
            .verticalScroll(rememberScrollState())
    ) {
        if (mode == 1) {
            Text(
                text = stringResource(id = R.string.addExistingStock),
                fontFamily = poppinsFontFamily,
                fontSize = 30.sp
            )
        } else {
            Text(
                text = stringResource(id = com.example.spendsavvy.R.string.sellStock),
                fontFamily = poppinsFontFamily,
                fontSize = 30.sp
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = stringResource(id = R.string.product),
            fontFamily = poppinsFontFamily,
            fontSize = 15.sp
        )

        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = it }
        ) {
            TextField(
                value = searchProduct,
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
                for (stock in stockDetails) {          //read from existing stock items
                    DropdownMenuItem(
                        text = {
                            Text(text = stock.productName)
                        },
                        onClick = {
                            searchProduct = stock.productName
                            isExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = stringResource(id = com.example.spendsavvy.R.string.origPrice),
            fontFamily = poppinsFontFamily,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(15.dp))

        for (stock in stockDetails) {
            if (searchProduct == stock.productName)
                Text(
                    text = stock.originalPrice.toString()
                )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = stringResource(id = com.example.spendsavvy.R.string.currentStock),
            fontFamily = poppinsFontFamily,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(15.dp))

        for (stock in stockDetails) {
            if (searchProduct == stock.productName)
                Text(
                    text = stock.quantity.toString()
                )
        }

        if (mode == 2) {
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = stringResource(id = com.example.spendsavvy.R.string.setStock) + "(RM)",
                fontFamily = poppinsFontFamily,
                fontSize = 15.sp
            )

            TextField(
                value = price,
                onValueChange = {
                    price = it
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Decimal
                ),
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

        if (mode == 1) {
            Text(
                text = stringResource(id = com.example.spendsavvy.R.string.qtyToAdd),
                fontFamily = poppinsFontFamily,
                fontSize = 15.sp
            )
        } else {
            Text(
                text = stringResource(id = com.example.spendsavvy.R.string.qtyToSell),
                fontFamily = poppinsFontFamily,
                fontSize = 15.sp
            )
        }
        TextField(
            value = qty,
            onValueChange = {
                qty = it
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
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
                    text = stringResource(id = com.example.spendsavvy.R.string.cancel),
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFontFamily
                )
            }

            Button(
                onClick = {
                    for (stock in stockDetails) {
                        if (searchProduct == stock.productName) {
                            if ((price.toDoubleOrNull() ?: 0.00) <= 0.00) {
                                Toast.makeText(
                                    context,
                                    "Invaid Input!\nPlease set a price that is more than RM 0.00",
                                    Toast.LENGTH_SHORT
                                ).show()

                            } else if ((qty.toIntOrNull() ?: 0) <= 0) {
                                Toast.makeText(
                                    context,
                                    "You must at least add 1 item",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                if (mode == 2) {                                        //sell existing stock
                                    if ((qty.toIntOrNull() ?: 0) > stock.quantity) {
                                        Toast.makeText(
                                            context,
                                            "You cannot only sell more than what you have\nAvailable quantity: ${stock.quantity}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        transactionViewModel.addTransactionToFirestore(
                                            Transactions(
                                                id = transactionViewModel.generateTransactionId(),
                                                amount = ((price.toDoubleOrNull()
                                                    ?: 0.0) * qty.toInt()),
                                                description = "Sell Stock",
                                                date = Date(),
                                                category = Category(
                                                    id = "CT0002",
                                                    imageUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/spendsavvy-5a2a8.appspot.com/o/images%2Fstock.png?alt=media&token=416dc2e0-caf2-4c9e-a664-2c0eceba49fb"),
                                                    categoryName = "Stock Sales",
                                                    categoryType = "Incomes"
                                                ),
                                                paymentMethod = searchAccount,
                                                transactionType = "Incomes"
                                            ),
                                            onSuccess = {
                                                walletViewModel.editStockDetails(
                                                    stock = stock,
                                                    updatedStockDetails = Stock(
                                                        stock.imageUri,
                                                        searchProduct,
                                                        stock.originalPrice,
                                                        stock.quantity - qty.toInt()
                                                    )
                                                )
                                            },
                                            onFailure = {
                                            }
                                        )

                                    }
                                } else {                                            //add existing stock
                                    transactionViewModel.addTransactionToFirestore(
                                        Transactions(
                                            id = transactionViewModel.generateTransactionId(),
                                            amount = ((stock.originalPrice) * qty.toInt()),
                                            description = "Purchase Stock",
                                            date = Date(),
                                            category = Category(
                                                id = "CT0014",
                                                imageUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/spendsavvy-5a2a8.appspot.com/o/images%2Fstock.png?alt=media&token=416dc2e0-caf2-4c9e-a664-2c0eceba49fb"),
                                                categoryName = "Stock Purchase",
                                                categoryType = "Expenses"
                                            ),
                                            paymentMethod = searchAccount,
                                            transactionType = "Expenses"
                                        ),
                                        onSuccess = {
                                            walletViewModel.editStockDetails(
                                                stock = stock,
                                                updatedStockDetails = Stock(
                                                    stock.imageUri,
                                                    searchProduct,
                                                    stock.originalPrice,
                                                    stock.quantity + qty.toInt()
                                                )
                                            )
                                        },
                                        onFailure = {
                                        }
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
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = if (mode == 2) stringResource(id = com.example.spendsavvy.R.string.sell) else stringResource(
                        id = com.example.spendsavvy.R.string.all
                    ),
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFontFamily
                )
            }
        }
    }
}
