package com.example.spendsavvy.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.animation.bounceClick
import com.example.spendsavvy.data.BankAccountData
import com.example.spendsavvy.ui.theme.poppinsFontFamily

@Composable
fun MoneyScreen(modifier: Modifier) {

    var isDialogPopUp = remember { mutableStateOf(false) }


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
            modifier = Modifier.fillMaxWidth(),
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

            Text(
                text = "RM 5000",
                fontFamily = poppinsFontFamily
            )
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
                text = "3 Accounts", /*calculate acc*/
                fontFamily = poppinsFontFamily
            )
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
            MoneyPopUpScreen(onCancelClick = { isDialogPopUp.value = false }, {})
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState", "RememberReturnType")
@Composable
fun MoneyPopUpScreen(
    onCancelClick: () -> Unit,          //nothing to be returned
    onConfirmClick: () -> Unit
) {
    val options = mutableStateListOf<String>("Cash", "Bank")
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
                    
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MoneyScreenPreview() {
    MoneyScreen(
        Modifier
            .fillMaxSize()
            .padding(20.dp)
    )

}