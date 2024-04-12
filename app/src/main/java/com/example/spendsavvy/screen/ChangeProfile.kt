package com.example.spendsavvy.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.R


@Composable
fun ChangeProfileScreen(modifier: Modifier = Modifier, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Image(
            painter = painterResource(R.drawable.add_image_icon),
            contentDescription = "User profile image",
            modifier = Modifier
                .size(125.dp)
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape)
                .background(Color.Gray),
        )

        Spacer(modifier = Modifier.height(15.dp)) // Spacer added here

        OutlinedTextFieldItem(
            label = "User Name",
            value = "User Name",
            onValueChange = { /* Implement logic to change the user name */ },
            height = 60.dp
        )

        OutlinedTextFieldItem(
            label = "Email Address",
            value = "Email Address",
            keyboardType = KeyboardType.Email,
            onValueChange = { /* Implement logic to change the email address */ },
            height = 60.dp
        )

        OutlinedTextFieldItem(
            label = "Phone Number",
            value = "Phone Number",
            keyboardType = KeyboardType.Phone,
            onValueChange = { /* Implement logic to change the phone number */ },
            height = 60.dp
        )

        OutlinedTextFieldItem(
            label = "Home Address",
            value = "Home Address",
            onValueChange = { /* Implement logic to change the home address */ },
            height = 110.dp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(text = "Save Changes")
        }
    }
}

@Composable
fun OutlinedTextFieldItem(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit,
    height: Dp = TextFieldDefaults.MinHeight
) {
    val (textValue, setTextValue) = remember { mutableStateOf(value) }

    OutlinedTextField(
        value = textValue,
        onValueChange = {
            setTextValue(it)
            onValueChange(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .background(Color.White)
            .height(height), // Adjusted height here
        textStyle = TextStyle(color = Color.Gray, fontSize = 20.sp),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        label = { Text(text = label, color = Color.Black) },
    )
}

@Preview(showBackground = true)
@Composable
fun ChangeProfileScreenPreview() {
    ChangeProfileScreen(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        navController = rememberNavController()
    )
}