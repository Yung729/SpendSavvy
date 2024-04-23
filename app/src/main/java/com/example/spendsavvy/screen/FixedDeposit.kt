package com.example.spendsavvy.screen

import android.annotation.SuppressLint
import android.widget.HorizontalScrollView
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
import androidx.compose.material3.Divider
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.components.bounceClick
import com.example.spendsavvy.data.BankAccountData
import com.example.spendsavvy.data.bankName
import com.example.spendsavvy.models.BankAccount
import com.example.spendsavvy.ui.theme.poppinsFontFamily

@Composable
fun FixedDepositScreen(modifier: Modifier, navController: NavController){

    var isDialogPopUp = remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(10.dp)) {
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "Fixed Deposit",
            fontFamily = poppinsFontFamily,
            fontSize = 15.sp
        )
        Text(
            text = "Asset that earn interest",
            fontSize = 10.sp,
            fontFamily = poppinsFontFamily,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Fixed Deposit Accounts",
                fontFamily = poppinsFontFamily)

            Text(text = "3 Accounts", /*calculate acc*/
                fontFamily = poppinsFontFamily)
        }

        Spacer(modifier = Modifier.height(15.dp))
        Divider(color = Color.Gray, thickness = 0.7.dp)
        Spacer(modifier = Modifier.height(15.dp))

        Box(modifier = Modifier.fillMaxSize()) {

            BankList(bankList = BankAccountData().loadBank())

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
                            text = "Add FD Account",
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }
                }
            }
            if (isDialogPopUp.value)
                FixedDepositPopUpScreen(onCancelClick = { isDialogPopUp.value = false }, {})
        }
    }
}

@Composable
fun BankCard(bankAccount: BankAccount,modifier: Modifier){
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )

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
                    text = bankAccount.bankName,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "RM ${bankAccount.balance}",
                    fontFamily = poppinsFontFamily,
                    color = Color.Gray
                    )
                Spacer(modifier = Modifier.height(15.dp))
                Divider(color = Color.Gray, thickness = 0.7.dp)
            }

        }
    }
}

@Composable
fun BankList(bankList: List<BankAccount>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(bankList) { bankAccount ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BankCard(
                    bankAccount = bankAccount,
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                )


            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState", "RememberReturnType")
@Composable
fun FixedDepositPopUpScreen(
    onCancelClick: () -> Unit,          //nothing to be returned
    onConfirmClick: () -> Unit
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    var searchBank by remember {
        mutableStateOf("Affin Bank Berhad")
    }

    var depositAmt by remember {
        mutableStateOf("")
    }

    var interestRate by remember {
        mutableStateOf("")
    }

    val options = mutableStateListOf<String>("3 Month", "6 Month", "1 Year")
    var selectedIndex by remember {
        mutableStateOf(0)
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
            Text(
                text = "Fixed Deposit",
                fontFamily = poppinsFontFamily,
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(start = 15.dp, top = 15.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ){
                SingleChoiceSegmentedButtonRow {
                    options.forEachIndexed{
                            index, option ->
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, top = 15.dp)
            ) {

                Text(
                    text = "Select your bank",
                    fontFamily = poppinsFontFamily,
                    fontSize = 15.sp
                )

                ExposedDropdownMenuBox(
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
                        for(i in bankName) {
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
                }
                Spacer(modifier = Modifier.height(15.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, top = 15.dp)
            ) {

                Text(text = "Deposit Amount",
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
                    text = "Interest Rate",
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
                            text = "0.00%",
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
            ){
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

@Composable
fun FdDetails(){

}

@Composable
@Preview(showBackground = true)
fun FixedDepositScreenPreview(){
    FixedDepositScreen(
        Modifier
            .fillMaxSize()
            .padding(20.dp),
        navController = rememberNavController())
}