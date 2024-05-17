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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.spendsavvy.R
import com.example.spendsavvy.models.UserData
import com.example.spendsavvy.navigation.Screen
import com.example.spendsavvy.repo.FireAuthRepository
import com.example.spendsavvy.viewModels.CategoryViewModel
import com.example.spendsavvy.viewModels.ProfileViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    profileViewModel: ProfileViewModel,
    fireAuthRepository: FireAuthRepository
) {
    var showDialog by remember { mutableStateOf(false) }
    val userData by profileViewModel.userData.observeAsState(UserData())

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
                            width = 1.dp, color = Color.Gray, shape = CircleShape
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
                text = stringResource(id = R.string.personalInfo),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 5.dp)
            )
            List(R.drawable.profile_icon, stringResource(id = com.example.spendsavvy.R.string.myProfile),
                navController,
                Screen.MyProfile.route)
        }

        item {
            Text(
                text = stringResource(id = com.example.spendsavvy.R.string.security),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 5.dp)
            )
            List(
                R.drawable.forgotpassword_icon,
                stringResource(id = com.example.spendsavvy.R.string.forgotPw),
                navController,
                Screen.ForgotPassword.route
            )
        }

        item {
            Text(
                text = stringResource(id = com.example.spendsavvy.R.string.tools),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 5.dp)
            )
            List(
                R.drawable.bills_icon,
                stringResource(id = com.example.spendsavvy.R.string.manageBill),
                navController,
                Screen.ManageBillsAndInstalment.route
            )
            List(
                R.drawable.calculator_icon,
                stringResource(id = com.example.spendsavvy.R.string.taxCalculator),
                navController,
                Screen.TaxCalculator.route
            )
            List(
                R.drawable.category_icon,
                stringResource(id = com.example.spendsavvy.R.string.manageCategory),
                navController,
                Screen.Category.route
            )
            List(
                R.drawable.staff_icon,
                stringResource(id = com.example.spendsavvy.R.string.manageStaff),
                navController,
                Screen.StaffScreen.route
            )
        }

        item {
            Text(
                text = stringResource(id = com.example.spendsavvy.R.string.general),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 5.dp)
            )
            List(
                R.drawable.bell_icon,
                stringResource(id = com.example.spendsavvy.R.string.notifications),
                navController,
                Screen.Notifications.route
            )
            List(
                R.drawable.language_icon,
                stringResource(id = com.example.spendsavvy.R.string.language),
                navController,
                Screen.Language.route
            )
            List(
                R.drawable.help_icon,
                stringResource(id = com.example.spendsavvy.R.string.help),
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
                    containerColor = Color.Transparent, contentColor = Color.Red
                )
            ) {
                Text(
                    text = stringResource(id = com.example.spendsavvy.R.string.logOut),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }

    if (showDialog) {
        AlertDialog(onDismissRequest = { showDialog = false }, title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                Image(painter = painterResource(R.drawable.cross_icon),
                    contentDescription = "",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { showDialog = false }
                        .padding(bottom = 10.dp))
                Text(
                    text = stringResource(id = com.example.spendsavvy.R.string.text_22),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }, confirmButton = {
            Button(
                onClick = {
                    fireAuthRepository.signOut()

                    navController.navigate("First") {
                        popUpTo(0) {}
                    }
                },
                modifier = Modifier.padding(end = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent, contentColor = Color.Red
                )
            ) {
                Text(text = stringResource(id = com.example.spendsavvy.R.string.logOut))
            }
        }, dismissButton = {
            Button(
                onClick = { showDialog = false }, modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(text = stringResource(id = com.example.spendsavvy.R.string.cancel))
            }
        })
    }
}

@Composable
fun List(imgId: Int, text: String, navController: NavController, route: String) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { navController.navigate(route = route) }
        .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = imgId),
            contentDescription = "",
            modifier = Modifier.size(25.dp)
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
        navController = rememberNavController(),
        profileViewModel = viewModel(),
        fireAuthRepository = FireAuthRepository(
            context = LocalContext.current,
            CategoryViewModel(LocalContext.current, false, ""),false
        )
    )
}
