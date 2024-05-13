package com.example.spendsavvy.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spendsavvy.models.FDAccount
import com.example.spendsavvy.viewModels.WalletViewModel

@Composable
fun FDEarnScreen(
    modifier: Modifier,
    navController: NavController,
    fdAccount: FDAccount
) {
    Column(
        modifier = Modifier.padding(start = 15.dp)
    ) {

        Text(fdAccount.bankName)

        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Column {
            //image
            Text(text = "")    //show fd amount //black

            Text(
                "Account Balance",
                color = Color.Gray
            )
            }
        }

        Row {
            Card(
                border = BorderStroke(2.dp, Color.Black)
            ) {
                Text(
                    "Interest Earned",
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "RM ${fdAccount.deposit * fdAccount.interestRate}",    //temporary
                    color = Color.Green
                )
            }

            Card(
                border = BorderStroke(2.dp, Color.Black)
            ) {
                Column {
                    Text("Duration")

                    //show how many days left
                    Text(
                        "1 Year",
                        color = Color.Gray
                    )

                    Spacer(modifier.height(5.dp))

                    Text("Interest Rate")

                    //show how many days left
                    Text(
                        "${fdAccount.interestRate} %",
                        color = Color.Gray
                    )
                }

            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "Record",
            fontSize = 15.sp)

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
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
                            "Back"
                        )
                    }
                }
            }
        }
    }
}