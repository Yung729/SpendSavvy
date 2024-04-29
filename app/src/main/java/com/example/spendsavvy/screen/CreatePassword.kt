package com.example.spendsavvy.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.models.UserData
import com.google.firebase.auth.FirebaseAuth


@Composable
fun CreatePassword(modifier: Modifier = Modifier, navController: NavController) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }
    var isPasswordValid by remember { mutableStateOf(false) }

    var userData by remember { mutableStateOf(UserData("", "", "", "", "", "")) }
    val auth = FirebaseAuth.getInstance()
    getUserData(auth.currentUser?.uid ?: "") { user ->
        userData = user
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = "Create a\nNew Password",
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(5.dp)
        )
        Text(
            text = "Enter your new password",
            color = Color.Gray,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
        )

        PasswordTextField(
            labelText = "New Password",
            value = oldPassword,
            onValueChange = { oldPassword = it },
            passwordVisible = passwordVisible,
            onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
            userData = userData
        )

        PasswordTextField(
            labelText = "Confirm Password",
            value = newPassword,
            onValueChange = { newPassword = it },
            passwordVisible = passwordVisible,
            onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
            userData = userData
        )

        CreatePasswordButton(
            oldPassword = oldPassword,
            newPassword = newPassword,
            showDialog = showDialog,
            isPasswordValid = isPasswordValid,
            onShowDialogChange = { showDialog = it },
            onIsPasswordValidChange = { isPasswordValid = it },
            navController
        )
    }
}
@Composable
fun CreatePasswordButton(
    oldPassword: String,
    newPassword: String,
    showDialog: Boolean,
    isPasswordValid: Boolean,
    onShowDialogChange: (Boolean) -> Unit,
    onIsPasswordValidChange: (Boolean) -> Unit,
    navController: NavController
) {
    var success by remember { mutableStateOf(false) }

    Button(
        onClick = {
            if (oldPassword.isNotEmpty() && newPassword.isNotEmpty() && passwordValidation(oldPassword) && passwordValidation(newPassword)) {
                if (newPassword == oldPassword) {
                    saveNewPasswordToFirestore(newPassword)
                    onShowDialogChange(true)
                    onIsPasswordValidChange(true)
                    success = true
                } else {
                    onShowDialogChange(true)
                    onIsPasswordValidChange(false)
                }
            } else {
                onShowDialogChange(true)
                onIsPasswordValidChange(false)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.DarkGray,
            contentColor = Color.White
        )
    ) {
        Text(
            text = "Save New Password",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
    }

    if (showDialog) {
        val title = if (isPasswordValid) "Success" else "Fail"
        val message = if (isPasswordValid) "New password has been created successfully" else "Unable to create new password \n Please Try Again"

        AlertDialog(
            onDismissRequest = { onShowDialogChange(false) },
            title = {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp,
                    color = if (isPasswordValid) Color.Green else Color.Red,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    text = message,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            onShowDialogChange(false)
                            if (isPasswordValid) {
                                navController.navigate("Settings")
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = if (isPasswordValid) "Continue" else "Try Again")
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CreatePasswordPreview() {
    CreatePassword(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        navController = rememberNavController()
    )
}