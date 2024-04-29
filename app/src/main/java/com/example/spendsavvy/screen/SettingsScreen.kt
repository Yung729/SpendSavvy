package com.example.spendsavvy.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.spendsavvy.R
import com.example.spendsavvy.models.UserData
import com.example.spendsavvy.navigation.Screen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SettingsScreen(modifier: Modifier = Modifier, navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }
    var userData by remember { mutableStateOf(UserData("","","", "", "", "")) }
    val auth = FirebaseAuth.getInstance()
    getUserData(auth.currentUser?.uid ?: "") { user ->
        userData = user
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
        contentPadding = PaddingValues(vertical = 2.dp)
    ) {
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = if (userData.photoURL!!.isNotEmpty()) {
                        rememberAsyncImagePainter(model = userData.photoURL)
                    } else {
                        painterResource(id = R.drawable.profile_icon)
                    },
                    contentDescription = "User profile image",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(
                            width = 1.dp,
                            color = Color.Gray,
                            shape = CircleShape
                        )
                )
                Text(
                    text = userData.userName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        item {
            Text(
                text = "Personal Info",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 5.dp)
            )
            List(R.drawable.profile_icon, "My Profile", navController, Screen.MyProfile.route)
        }

        item {
            Text(
                text = "Security",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 5.dp)
            )
            List(
                R.drawable.changepassword_icon,
                "Change Password",
                navController,
                Screen.ChangePassword.route
            )
            List(
                R.drawable.forgotpassword_icon,
                "Forgot Password",
                navController,
                Screen.ForgotPassword.route
            )
        }

        item {
            Text(
                text = "Tools",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 5.dp)
            )
            List(
                R.drawable.bills_icon,
                "Manage Bills and Instalments",
                navController,
                Screen.ManageBillsAndInstalment.route
            )
            List(
                R.drawable.calculator_icon,
                "Tax Calculator",
                navController,
                Screen.TaxCalculator.route
            )
            List(
                R.drawable.category_icon,
                "Manage Category",
                navController,
                Screen.Category.route
            )
        }

        item {
            Text(
                text = "General",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 5.dp)
            )
            List(
                R.drawable.bell_icon,
                "Notifications",
                navController,
                Screen.Notifications.route
            )
            List(
                R.drawable.language_icon,
                "Languages", navController,
                Screen.Language.route
            )
            List(
                R.drawable.help_icon,
                "Help and Support",
                navController,
                Screen.HelpAndSupport.route
            )
        }

        item {
            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Red
                )
            ) {
                Text(
                    text = "Log Out",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
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
                    onClick = { navController.navigate(Screen.Login.route) },
                    modifier = Modifier
                        .padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Red
                    )
                ) {
                    Text(text = "Log Out")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false },
                    modifier = Modifier
                        .padding(start = 8.dp)
                ) {
                    Text(text = "Cancel")
                }
            }
        )
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
