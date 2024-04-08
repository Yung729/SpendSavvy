package com.example.spendsavvy.screen

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.animation.bounceClick
import com.example.spendsavvy.data.BankAccountData
import com.example.spendsavvy.State.BankAccount
import com.example.spendsavvy.ui.theme.poppinsFontFamily

@Composable
fun MoneyScreen(modifier: Modifier){

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
        ){
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

            Text(text = "3 Accounts", /*calculate acc*/
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
                        onClick = { /*TODO*/ },
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