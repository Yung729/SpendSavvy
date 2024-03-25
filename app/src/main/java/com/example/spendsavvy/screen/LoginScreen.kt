package com.example.spendsavvy.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.text.AnnotatedString
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
import com.example.spendsavvy.animation.bounceClick
import com.example.spendsavvy.component.ButtonComponent
import com.example.spendsavvy.navigation.Screen
import com.example.spendsavvy.ui.theme.ButtonColor
import com.example.spendsavvy.ui.theme.HeaderTitle
import com.example.spendsavvy.ui.theme.poppinsFontFamily
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


@Composable
fun LoginScreen(modifier: Modifier = Modifier, navController: NavController, auth: FirebaseAuth) {

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
            text = "Login",
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
            label = { Text(text = "Email") },
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = "Email")
            },
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp),
            shape = RoundedCornerShape(15.dp),
            singleLine = true,

            )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
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
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener() { task ->
                if (task.isSuccessful) {

                    val user = auth.currentUser
                    navController.navigate(route = "EnterMainScreen") {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }

                } else if (!task.isSuccessful) {
                    Toast.makeText(
                        context, "Unsuccessful to Sign In Account", Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }, text = "LOGIN")


        ClickableText(
            text = AnnotatedString("Forgot Password ?"),
            onClick = {

            },
            style = TextStyle(
                color = ButtonColor, fontFamily = poppinsFontFamily
            ),
            modifier = Modifier.bounceClick()
        )

        ClickableText(
            text = AnnotatedString("Register Account ?"),
            onClick = {
                navController.navigate(route = Screen.SignUp.route)
            },
            style = TextStyle(
                color = ButtonColor, fontFamily = poppinsFontFamily
            ),
            modifier = Modifier.bounceClick()
        )

    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        navController = rememberNavController(),
        auth = Firebase.auth
    )
}