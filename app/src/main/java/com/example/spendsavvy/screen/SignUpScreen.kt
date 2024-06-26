package com.example.spendsavvy.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.R
import com.example.spendsavvy.components.ButtonComponent
import com.example.spendsavvy.navigation.Screen
import com.example.spendsavvy.repo.FireAuthRepository
import com.example.spendsavvy.ui.theme.ButtonColor
import com.example.spendsavvy.ui.theme.HeaderTitle
import com.example.spendsavvy.ui.theme.poppinsFontFamily
import com.example.spendsavvy.viewModels.CategoryViewModel

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    fireAuthRepository: FireAuthRepository
) {

    var photoURL by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var phoneNo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val passIcon = if (passwordVisible)
        painterResource(id = R.drawable.show_pass)
    else
        painterResource(id = R.drawable.hide_pass)


    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.signUp_text),
            fontFamily = poppinsFontFamily,
            textAlign = TextAlign.Center,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = HeaderTitle
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text(stringResource(id = R.string.userName)) },
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = "User Name")
            },
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp),
            shape = RoundedCornerShape(15.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = phoneNo,
            onValueChange = { phoneNo = it },
            label = { Text(stringResource(id = R.string.phoneNo)) },
            leadingIcon = {
                Icon(Icons.Default.AccountBox, contentDescription = "Phone Number")
            },
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp),
            shape = RoundedCornerShape(15.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            isError = phoneNo.isEmpty() && !phoneValidation(phoneNo)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(id = R.string.email)) },
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = "Email")
            },
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp),
            shape = RoundedCornerShape(15.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = email.isEmpty() && !emailValidation(email)
        )


        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(id = R.string.password)) },
            leadingIcon = {
                Icon(Icons.Default.Info, contentDescription = "password")
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = passIcon,
                        contentDescription = "PassIcon",
                        modifier = Modifier.size(35.dp)
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp),
            shape = RoundedCornerShape(15.dp),
            singleLine = true,
            isError = passwordValidation(password)
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text(stringResource(id = R.string.confirmPw)) },
            leadingIcon = {
                Icon(Icons.Default.Info, contentDescription = "Confirm password")
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = passIcon,
                        contentDescription = "PassIcon",
                        modifier = Modifier.size(35.dp)
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp),
            shape = RoundedCornerShape(15.dp),
            singleLine = true,
            isError = (password != confirmPassword),

            )

        ButtonComponent(
            onButtonClick = {
                isError = false

                // Check for blank fields
                if (email.isBlank() || password.isBlank() || confirmPassword.isBlank() || userName.isBlank() || phoneNo.isBlank()) {
                    Toast.makeText(
                        context,
                        "All fields are required",
                        Toast.LENGTH_SHORT
                    ).show()
                    isError = true
                } else {
                    if (!emailValidation(email)) {
                        Toast.makeText(
                            context,
                            "Invalid Email Format",
                            Toast.LENGTH_SHORT
                        ).show()
                        isError = true
                    } else if (!passwordValidation(password)) {
                        Toast.makeText(
                            context,
                            "Minimum 8 Characters Password Required",
                            Toast.LENGTH_SHORT
                        ).show()
                        isError = true
                    } else if (password != confirmPassword) {
                        Toast.makeText(
                            context,
                            "Passwords Do Not Match",
                            Toast.LENGTH_SHORT
                        ).show()
                        isError = true
                    } else if (!phoneValidation(phoneNo)) {
                        Toast.makeText(
                            context,
                            "Invalid Phone Number, must need 10 numbers",
                            Toast.LENGTH_SHORT
                        ).show()
                        isError = true
                    }
                }

                // If no errors, proceed with sign-up
                if (!isError) {
                    fireAuthRepository.signUp(email, password, userName, phoneNo)
                    navController.navigate(route = Screen.Login.route)
                }
            },
            text = stringResource(id = R.string.signUp_text)
        )

        Text(
            text = stringResource(id = R.string.haveAcc),
            style = TextStyle(
                color = ButtonColor,
                fontFamily = poppinsFontFamily
            ),
            modifier = Modifier.clickable {
                navController.navigate(route = Screen.Login.route)
            }
        )
    }
}


//validation
fun emailValidation(email: String): Boolean {
    return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex())
}

fun passwordValidation(password: String): Boolean {
    if (password.isEmpty())
        return false
    else if (password.count() < 8)
        return false

    return true
}

fun phoneValidation(phoneNo: String): Boolean {
    val pattern = "^\\+?0?(\\d{2})[- ]?(\\d{3})[- ]?(\\d{4})\$"
    return phoneNo.matches(pattern.toRegex())
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp), navController = rememberNavController(),
        fireAuthRepository = FireAuthRepository(
            LocalContext.current,  categoryViewModel = CategoryViewModel(
                LocalContext.current, false, ""
            ),false
        )
    )
}

