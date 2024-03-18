package com.example.spendsavvy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.ui.theme.ButtonColor
import com.example.spendsavvy.ui.theme.CardColor
import com.example.spendsavvy.ui.theme.HeaderTitle
import com.example.spendsavvy.ui.theme.poppinsFontFamily

@Composable
fun OverviewScreen(modifier: Modifier = Modifier, navController: NavController) {


    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Overview",
                fontFamily = poppinsFontFamily,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = HeaderTitle,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Icon(
                Icons.Default.Notifications,
                contentDescription = "Notifications",
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "Hi, User",
            fontFamily = poppinsFontFamily,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = HeaderTitle
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = CardColor,
                contentColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            ),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = "Total Balance",
                    modifier = Modifier,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )

                Text(
                    text = "RM 5000.00",
                    modifier = Modifier,
                    textAlign = TextAlign.Center,

                    )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column {
                        Text(
                            text = "Income",
                            modifier = Modifier,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )

                        Text(
                            text = "RM 6000.00",
                            modifier = Modifier,
                            textAlign = TextAlign.Center,
                        )
                    }

                    Column {
                        Text(
                            text = "Expenses",
                            modifier = Modifier,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )

                        Text(
                            text = "RM 1000.00",
                            modifier = Modifier,
                            textAlign = TextAlign.Center,
                        )
                    }


                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Record",
                fontFamily = poppinsFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = HeaderTitle
            )

            Text(
                text = "See All",
                fontFamily = poppinsFontFamily,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Blue
            )
        }

        //need to a fragment to show the list


    }
}

@Preview(showBackground = true)
@Composable
fun OverviewScreenPreview() {
    OverviewScreen(
        Modifier
            .fillMaxSize()
            .padding(20.dp), navController = rememberNavController()
    )
}