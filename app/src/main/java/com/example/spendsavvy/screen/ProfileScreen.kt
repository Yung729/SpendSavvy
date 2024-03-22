package com.example.spendsavvy.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.spendsavvy.ui.theme.ButtonColor

@Composable
fun ProfileScreen(modifier: Modifier = Modifier, navController: NavController) {
    OutlinedButton(onClick = { /*TODO*/ },
        modifier = Modifier
            .padding(bottom = 10.dp, top = 10.dp)
            .bounceClick(),
        colors = ButtonDefaults.buttonColors(
            containerColor = ButtonColor
        )) {
        Text(text = "Log Out")
    }
}