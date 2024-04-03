package com.example.spendsavvy.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.R
import com.example.spendsavvy.navigation.Screen
import com.example.spendsavvy.ui.theme.HeaderTitle
import com.example.spendsavvy.ui.theme.poppinsFontFamily


@Composable
fun SettingsScreen(modifier: Modifier = Modifier, navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
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
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(40.dp)
            )
            Text(
                text = "Settings",
                fontFamily = poppinsFontFamily,
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold,
                color = HeaderTitle,
                modifier = Modifier
                    .padding(end = 130.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            Image(
                painter = painterResource(R.drawable.add_image_icon),
                contentDescription = "User profile image",
                modifier = Modifier
                    .size(125.dp)
                    .padding(vertical = 16.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
                    .background(Color.Gray),
            )

            Text(
                text = "User nameeee",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            Row(
                modifier = Modifier.padding(vertical = 5.dp)
            ) {
                Text(
                    text = "Personal Info",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            List(R.drawable.profile_icon, "User Profile", navController, Screen.MyProfile.route)
//            List(R.drawable.expense_icon, "Recent Expenses",navController, Screen.Stock.route)

            Row(
                modifier = Modifier.padding(vertical = 5.dp)
            ) {
                Text(
                    text = "Security",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                )
            }

            List(
                R.drawable.changepassword_icon,
                "Change Password",
                navController,
                Screen.Stock.route
            )
            List(
                R.drawable.forgotpassword_icon,
                "Forgot Password",
                navController,
                Screen.Stock.route
            )

            Row(
                modifier = Modifier.padding(vertical = 5.dp)
            ) {
                Text(
                    text = "General",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                )
            }

            List(R.drawable.bell_icon, "Notifications", navController, Screen.Stock.route)
            List(R.drawable.language_icon, "Languages", navController, Screen.Stock.route)
            List(R.drawable.help_icon, "Help and Support", navController, Screen.Stock.route)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Logout",
                    fontSize = 20.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

@Composable
fun List(imgId: Int, text: String, navController: NavController, route: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate(route = route) }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imgId),
            contentDescription = "",
            modifier = Modifier
                .size(30.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f),
            color = Color.Gray

        )
    }
    LineDivider()
}

@Composable
fun LineDivider() {
    Divider(
        color = Color.Gray,
        thickness = 0.8.dp,
        modifier = Modifier
            .padding(vertical = 10.dp)
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
    )
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