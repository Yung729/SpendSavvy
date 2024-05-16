package com.example.spendsavvy.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spendsavvy.R
import com.example.spendsavvy.models.UserData
import com.example.spendsavvy.repo.FireAuthRepository
import com.example.spendsavvy.screen.start.emailValidation
import com.example.spendsavvy.viewModels.ProfileViewModel


@Composable
fun ForgotPassword(
    modifier: Modifier = Modifier,
    navController: NavController,
    profileViewModel: ProfileViewModel,
    fireAuthRepository: FireAuthRepository
) {

    val userData by profileViewModel.userData.observeAsState(UserData())
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Top,
    ) {

        Text(
            text = stringResource(id = R.string.forgotPw),
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(CenterHorizontally)
        )
        Text(
            text = stringResource(id = R.string.text_1),
            color = Color.Gray,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 20.dp)
        )

        var email by remember { mutableStateOf("") }

        EmailTextField(
            email = email,
            onEmailChange = { newEmail -> email = newEmail },
            label = stringResource(id = R.string.text_2)
        )

        Button(
            onClick = {
                if (emailValidation(email)) {
                    /*if (email == userData.email) {
                        navController.navigate(Screen.CreatePassword.route)
                    } else {
                        Toast.makeText(
                            context,
                            "User Email incorrect",
                            Toast.LENGTH_SHORT
                        ).show()
                    }*/

                    fireAuthRepository.resetPassword(email)
                    Toast.makeText(
                        context,
                        "Password recovery instructions sent to your email",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Toast.makeText(
                        context,
                        "Please enter a valid email",
                        Toast.LENGTH_SHORT
                    ).show()
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
                text = stringResource(id = R.string.next),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun EmailTextField(
    email: String,
    onEmailChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = label)
        OutlinedTextField(
            value = email,
            onValueChange = { onEmailChange(it) },
            label = { Text(stringResource(id = R.string.email)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )
    }
}


/*

@Preview(showBackground = true)
@Composable
fun ForgotPasswordPreview() {
    ForgotPassword(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        navController = rememberNavController()
    )
}*/
