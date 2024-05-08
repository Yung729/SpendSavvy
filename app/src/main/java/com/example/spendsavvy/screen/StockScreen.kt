package com.example.spendsavvy.screen

/*import com.example.spendsavvy.data.StockData*/
/*import com.example.spendsavvy.data.toyNames*/
import android.annotation.SuppressLint
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.spendsavvy.components.bounceClick
import com.example.spendsavvy.models.Stock
import com.example.spendsavvy.ui.theme.poppinsFontFamily
import com.example.spendsavvy.viewModels.WalletViewModel

@Composable
fun StockScreen(
    modifier: Modifier = Modifier,
    stockViewModel: WalletViewModel
    ) {

    var isSelectionPopUp by remember {
        mutableStateOf(false)
    }

    var isDialogPopUp by remember {
        mutableStateOf(false)
    }

    val stockListLive by stockViewModel.stockListLive.observeAsState(initial = emptyList())
    val totalPriceStock by stockViewModel.totalPriceStock.observeAsState(initial = 0.00)

    // Calculate total stock balance
    /*    val totalStockBalance = remember {
            StockData().loadStock().sumOf { it.originalPrice * it.quantity }
        }*/

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Stock Account",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Justify,
                    fontFamily = poppinsFontFamily,
                    fontSize = 15.sp,
                )

                Text(
                    text = "Add and Sell Stocks",
                    color = Color.Gray,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Justify
                )
            }

        }

        Spacer(modifier = Modifier.height(30.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White, //later need change to brush color
                contentColor = Color.Black
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            ),
            shape = RoundedCornerShape(15.dp), modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(15.dp)) {
                Text(
                    text = "Stock Balance",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = poppinsFontFamily,
                    fontSize = 15.sp,
                )

                Text(text = "RM $totalPriceStock")
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Stock",
            fontFamily = poppinsFontFamily,
            fontSize = 15.sp
        )

        Box(modifier = Modifier.fillMaxSize()) {
            StockList(stockList = stockListLive)

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
                        onClick = { isSelectionPopUp = true },
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .bounceClick()
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "Add Stock",
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }

                    OutlinedButton(
                        onClick = { isDialogPopUp = true },
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .bounceClick()
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Sell Stock",
                            textAlign = TextAlign.Center,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }

    if (isSelectionPopUp)
        AddStockSelectionScreen(onCancelClick = { isSelectionPopUp = false }, {})
    else if (isDialogPopUp)
        EditExistingStockScreen(onCancelClick = { isDialogPopUp = false }, {}, 2)
}


@Composable
fun StockCard(stock: Stock, modifier: Modifier = Modifier) {

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(width = 1.dp, Color.Black)
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
                    text = stock.productName,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "RM ${(stock.originalPrice * stock.quantity)}",
                    fontSize = 10.sp
                )
            }


            Text(
                text = "Qty : ${stock.quantity}",
                fontWeight = FontWeight.SemiBold,
                color = Color.Green,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState", "RememberReturnType")
@Composable
fun AddStockSelectionScreen(
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit
) {
    var isAddNewStock by remember {
        mutableStateOf(false)
    }

    var isAddExistingStock by remember {
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
                .fillMaxWidth(0.85f)
                .border(1.dp, color = Color.Gray, shape = RoundedCornerShape(15.dp))
                .shadow(elevation = 15.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, top = 15.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(15.dp),
                    horizontalArrangement = Arrangement.spacedBy(30.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            isAddNewStock = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(
                            text = "New",
                            fontWeight = FontWeight.Bold,
                            fontFamily = poppinsFontFamily
                        )
                    }

                    Button(
                        onClick = {
                            isAddExistingStock = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(
                            text = "Existing",
                            fontWeight = FontWeight.Bold,
                            fontFamily = poppinsFontFamily
                        )
                    }
                }
            }
        }
    }


    if (isAddNewStock)
        AddNewStockScreen(onCancelClick = { isAddNewStock = false }, onConfirmClick = {})
    else if (isAddExistingStock) {
        EditExistingStockScreen(
            onCancelClick = { isAddExistingStock = false },
            onConfirmClick = { },
            scene = 1
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState", "RememberReturnType")
@Composable
fun AddNewStockScreen(
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit
) {

    var stockName by remember {
        mutableStateOf("")
    }

    var price by remember {
        mutableStateOf("")
    }

    var qty by remember {
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
                Text(
                    text = "Add New Product",
                    fontFamily = poppinsFontFamily,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(30.dp))

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
                            text = "0",
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
                            text = "0",
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
                            onConfirmClick()
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
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState", "RememberReturnType")
@Composable
fun EditExistingStockScreen(
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
    scene: Int      //scene 1 = add existing stock, scene 2 = sell existing stock
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    var searchProduct by remember {
        mutableStateOf("Blaze")
    }

    var price by remember {
        mutableStateOf("")
    }

    var qty by remember {
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
                if (scene == 1) {
                    Text(
                        text = "Add Existing Stock",
                        fontFamily = poppinsFontFamily,
                        fontSize = 15.sp
                    )
                } else {
                    Text(
                        text = "Sell Stock",
                        fontFamily = poppinsFontFamily,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = "Product",
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
                        /*for(i in toyNames) {          //read from existing stock items
                            DropdownMenuItem(
                                text = {
                                    Text(text = i)
                                },
                                onClick = {
                                    searchProduct = i
                                    isExpanded = false
                                }
                            )
                        }*/
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

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
                            text = "0",
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
                            onConfirmClick()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(
                            text = "Sell",
                            fontWeight = FontWeight.Bold,
                            fontFamily = poppinsFontFamily
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun StockList(stockList: List<Stock>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(stockList) { item: Stock ->
            StockCard(
                stock = item,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StockScreenPreview() {
    StockScreen(
        Modifier
            .fillMaxSize()
            .padding(20.dp),
        stockViewModel = viewModel()
    )

}
