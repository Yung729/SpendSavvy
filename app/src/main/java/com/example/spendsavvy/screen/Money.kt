package com.example.spendsavvy.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun MoneyScreen(modifier: Modifier = Modifier, navController: NavController){
    Spacer(modifier = Modifier.height(30.dp))
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ){
        Column {
            Text(text = "Cash")
            Text(
                text = "Includes cash money , bank accounts and eWallet",
                color = Color.Gray,
                fontSize = 10.sp
            )
        }

    }
    Spacer(modifier = Modifier.height(30.dp))

}

@Preview(showBackground = true)
@Composable
fun MoneyScreenPreview() {
    MoneyScreen(
        Modifier
            .fillMaxSize()
            .padding(20.dp), navController = rememberNavController()
    )

}