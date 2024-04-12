package com.example.spendsavvy.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun TaxCalculator(modifier: Modifier = Modifier, navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
            .padding(horizontal = 20.dp), // Padding added here
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        TextFieldItem(
            label = "Initial Amount",
            value = "Enter your amount",
            onValueChange = { /* Implement logic to change the user name */ },
            height = 50.dp
        )

        TextFieldItem(
            label = "Tax Year",
            value = "2022",
            keyboardType = KeyboardType.Email,
            onValueChange = { /* Implement logic to change the email address */ },
            height = 50.dp
        )

        TextFieldItem(
            label = "Phone Number",
            value = "Phone Number",
            keyboardType = KeyboardType.Phone,
            onValueChange = { /* Implement logic to change the phone number */ },
            height = 50.dp
        )

        TextFieldItem(
            label = "Income Period",
            value = "Monthly / Annually",
            onValueChange = { /* Implement logic to change the home address */ },
            height = 50.dp
        )

        TextFieldItem(
            label = "Job Type",
            value = "Salaried / Business & AOP",
            onValueChange = { /* Implement logic to change the home address */ },
            height = 50.dp
        )

        LineDivider()

        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = "Monthly",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )


            Text(
                text = "Annually",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

        }

        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Add SpaceBetween arrangement
        ) {
            TextField(
                value = "",
                onValueChange = { },
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                placeholder = { Text(text = "Enter value") }
            )

            Spacer(modifier = Modifier.width(30.dp)) // Add Spacer with desired width

            TextField(
                value = "",
                onValueChange = { },
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                placeholder = { Text(text = "Enter value") }
            )
        }

        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Add SpaceBetween arrangement
        ) {
            TextField(
                value = "",
                onValueChange = { },
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                placeholder = { Text(text = "Enter value") }
            )

            Spacer(modifier = Modifier.width(30.dp)) // Add Spacer with desired width

            TextField(
                value = "",
                onValueChange = { },
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                placeholder = { Text(text = "Enter value") }
            )
        }

        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Add SpaceBetween arrangement
        ) {
            TextField(
                value = "",
                onValueChange = { },
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                placeholder = { Text(text = "Enter value") }
            )

            Spacer(modifier = Modifier.width(30.dp)) // Add Spacer with desired width

            TextField(
                value = "",
                onValueChange = { },
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                placeholder = { Text(text = "Enter value") }
            )
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
            ) {
                Text(text = "Calculate")
            }
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
            ) {
                Text(text = "Add in Expense")
            }
        }
    }
}

@Composable
fun TextFieldItem(
    label: String,
    value: String,
    height: Dp = TextFieldDefaults.MinHeight,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    val (textValue, setTextValue) = remember { mutableStateOf(value) }

    Column(
        modifier = Modifier.padding(bottom = 20.dp)
    ) {
        Text(
            text = label,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        TextField(
            value = textValue,
            onValueChange = {
                setTextValue(it)
                onValueChange(it)
            },
            modifier = Modifier
                .height(height)
                .fillMaxWidth(),
            textStyle = TextStyle(color = Color.Gray, fontSize = 15.sp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        )
    }
}


@Preview(showBackground = true)
@Composable
fun TaxCalculatorPreview() {
    TaxCalculator(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        navController = rememberNavController()
    )
}