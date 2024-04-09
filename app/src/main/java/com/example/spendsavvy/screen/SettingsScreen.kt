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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

    var showDialog by remember { mutableStateOf(false) }

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
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.padding(vertical = 5.dp)
        ) {
            Text(
                text = "Personal Info",
                fontSize = 18.sp,
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
                fontSize = 18.sp,
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
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
            )
        }

        List(R.drawable.bell_icon, "Notifications", navController, Screen.Stock.route)
        List(R.drawable.language_icon, "Languages", navController, Screen.Stock.route)
        List(R.drawable.help_icon, "Help and Support", navController, Screen.Stock.route)

        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Red
            )
        ) {
            Text(
                text = "Log Out",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false }, // Dismiss dialog when clicked outside
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Image(
                            painter = painterResource(R.drawable.cross_icon),
                            contentDescription = "",
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { showDialog = false }
                                .padding(bottom = 10.dp)
                        )
                        Text(
                            text = "Are you sure you want to\nLog Out ?",
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showDialog = false },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Red)
                    ) {
                        Text(text = "Log Out")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialog = false },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        Text(text = "Cancel")
                    }
                }
            )
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
                .size(25.dp)
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
    HorizontalDivider(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .padding(horizontal = 10.dp)
            .fillMaxWidth(),
        thickness = 0.8.dp,
        color = Color.Gray
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