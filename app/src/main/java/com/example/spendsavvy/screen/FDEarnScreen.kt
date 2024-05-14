package com.example.spendsavvy.screen

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spendsavvy.components.bounceClick
import com.example.spendsavvy.models.Cash
import com.example.spendsavvy.models.FDAccount
import com.example.spendsavvy.navigation.Screen
import com.example.spendsavvy.ui.theme.poppinsFontFamily
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

            Text(
                "${fdAccount.bankName} - FD",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        //image

                        Text(text = "RM ${fdAccount.deposit}")

                        Text(
                            "Account Balance",
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Card(
                    border = BorderStroke(2.dp, Color.Black),
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp)
                ) {
                    Column {

                        Text(
                            "Interest Earned",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 3.dp)
                        )

                        Text(
                            "RM ${fdAccount.deposit * fdAccount.interestRate / 100}",    //temporary
                            color = Color.Green,
                            modifier = Modifier.padding(start = 10.dp, top = 3.dp, end = 10.dp, bottom = 10.dp)
                        )

                    }

                }

                Card(
                    border = BorderStroke(2.dp, Color.Black),
                    modifier = Modifier
                        .padding(start = 50.dp)
                ) {
                    Column {

                        Text(
                            "Duration",
                            fontSize = 15.sp,
                            modifier = Modifier.padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 3.dp)
                        )

                        //show how many days left
                        Text(
                            "1 Year",
                            color = Color.Gray,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(start = 10.dp, top = 3.dp, end = 10.dp, bottom = 10.dp)
                        )

                        Text(
                            "Interest Rate",
                            fontSize = 15.sp,
                            modifier = Modifier.padding(start = 10.dp, top = 3.dp, end = 10.dp, bottom = 3.dp)
                        )

                        //show how many days left
                        Text(
                            "${fdAccount.interestRate} %",
                            fontSize = 15.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 10.dp, top = 3.dp, end = 10.dp, bottom = 10.dp)
                        )
                    }

                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "Record",
                fontSize = 20.sp
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                //FDTransferList()

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
                            onClick = {
                                navController.navigateUp()
                            },
                            modifier = Modifier
                                .padding(bottom = 10.dp)
                                .bounceClick()
                                .fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black
                            )
                        ) {
                            Text(
                                text = "Back",
                                textAlign = TextAlign.Center,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

}

@Composable
fun FDTransferList(

) {
    LazyColumn {
        /*items() { item: Cash ->
            if (item.typeName != "Cash") {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item.,
                        fontFamily = poppinsFontFamily,
                        fontSize = 15.sp
                    )

                    Text(
                        text = "RM ${item.}",
                        fontFamily = poppinsFontFamily,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))
                Divider(color = Color.Gray, thickness = 0.7.dp)
                Spacer(modifier = Modifier.height(15.dp))
            }
        }*/
    }
}