package com.example.spendsavvy.screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.R
import com.example.spendsavvy.ui.theme.HeaderTitle
import com.example.spendsavvy.ui.theme.poppinsFontFamily


@Composable
fun SettingsScreen(modifier: Modifier = Modifier, navController: NavController) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                Icons.Default.KeyboardArrowLeft,
                contentDescription = "Up",
                modifier = Modifier.align(Alignment.CenterVertically)
            )
//yh
            Text(
                text = "Settings",
                fontFamily = poppinsFontFamily,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = HeaderTitle,
                modifier = Modifier
                    .padding(end = 130.dp)
            )
        }

        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
        ) {
            Image(
                painter = painterResource(R.drawable.add_image_icon),
                contentDescription = "user profile image",
                modifier = Modifier
                    .size(100.dp)
                    .padding(vertical = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
//test2

            Text(
                text = "User nameeee",
                fontFamily = poppinsFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            Row(
                modifier = Modifier
                    .padding(vertical = 5.dp)
            ) {
                Text(
                    text = "User Information",
                    fontFamily = poppinsFontFamily,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            List(R.drawable.profile_icon, "User Profile")
            List(R.drawable.expense_icon, "Recent Expenses")

            Row(
                modifier = Modifier
                    .padding(vertical = 5.dp)
            ) {
                Text(
                    text = "Security",
                    fontFamily = poppinsFontFamily,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Normal,
                )
            }

            List(R.drawable.changepassword_icon, "Change Password")
            List(R.drawable.forgotpassword_icon, "Forgot Password")

            Row(
                modifier = Modifier
                    .padding(vertical = 5.dp)
            ) {
                Text(
                    text = "General",
                    fontFamily = poppinsFontFamily,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Normal,
                )
            }

            List(R.drawable.bell_icon, "Notifications")
            List(R.drawable.language_icon, "Language")
            List(R.drawable.help_icon, "Help and Support")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Logout",
                    fontFamily = poppinsFontFamily,
                    fontSize = 25.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Normal,
                )
            }
        }
    }
}

@Composable
fun List(imgId: Int, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imgId),
            contentDescription = "",
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            fontFamily = poppinsFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.weight(1f) // Occupy remaining space
        )
    }
}



@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        navController = rememberNavController()
    )
}