package com.example.spendsavvy.screen

import android.content.Context
import android.os.Build.VERSION_CODES.TIRAMISU
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
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
import com.example.spendsavvy.viewModels.MainViewModel


@RequiresApi(TIRAMISU)
@Composable
fun LoginScreen(
    context : Context,
    modifier: Modifier = Modifier,
    navController: NavController,
    fireAuthRepository: FireAuthRepository
) {


    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val passIcon = if (passwordVisible) painterResource(id = R.drawable.show_pass)
    else painterResource(id = R.drawable.hide_pass)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.login_text),
            fontFamily = poppinsFontFamily,
            textAlign = TextAlign.Center,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = HeaderTitle
        )

        Spacer(modifier = Modifier.height(20.dp))

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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
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
            singleLine = true
        )

        ButtonComponent(onButtonClick = {

            if (email.isNotBlank() && password.isNotBlank()){
                fireAuthRepository.signIn(email = email, password = password){
                    val mainViewModel = MainViewModel(context, true, fireAuthRepository.getCurrentUserId())
                    mainViewModel.syncDatabase()

                    navController.navigate("Second") {
                        popUpTo(0) {}

                    }
                }
            }else {
                Toast.makeText(
                    context, "Please fill in all the field", Toast.LENGTH_SHORT
                ).show()
            }




        }, text = stringResource(id = R.string.login_text))



        Text(
            text = stringResource(id = R.string.forgotPw),
            style = TextStyle(
                color = ButtonColor,
                fontFamily = poppinsFontFamily
            ),
            modifier = Modifier.clickable {
                navController.navigate(route = Screen.ForgotPassword.route)
            }
        )

        Text(
            text = stringResource(id = R.string.registerAcc),
            style = TextStyle(
                color = ButtonColor,
                fontFamily = poppinsFontFamily
            ),
            modifier = Modifier.clickable {
                navController.navigate(route = Screen.SignUp.route)
            }
        )


    }
}


@RequiresApi(TIRAMISU)
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() = LoginScreen(
    context = LocalContext.current,
    modifier = Modifier
        .fillMaxSize()
        .padding(20.dp),
    navController = rememberNavController(),
    fireAuthRepository = FireAuthRepository(
        LocalContext.current, categoryViewModel = CategoryViewModel(
            LocalContext.current, false, ""
        ),
        true
    )
)