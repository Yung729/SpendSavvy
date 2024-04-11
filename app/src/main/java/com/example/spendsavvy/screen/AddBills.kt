package com.example.spendsavvy.screen

import android.widget.DatePicker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.util.Date


@Composable
fun AddBills(modifier: Modifier = Modifier, navController: NavController) {

    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf<Date?>(null) }
    var selectedDuration by remember { mutableStateOf("") }

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = "Enter your bill details below, so we can \nnotify you in time for upcoming bills.",
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 20.dp)
        )

        Text(
            text = "Title",
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
        )
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    // Move focus to the next field
                }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Amount",
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
        )
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    // Move focus to the next field
                }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        DropdownMenu(
            expanded = false, // Change to true when clicked
            onDismissRequest = { /* Dismiss the dropdown menu */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            // Populate the dropdown list with categories
            // For simplicity, I'm hardcoding some categories here
//            DropdownMenuItem(onClick = {
//                category = "Category 1"
//            }) {
//                Text("Category 1")
//            }
//            DropdownMenuItem(onClick = {
//                category = "Category 2"
//            }) {
//                Text("Category 2")
//            }
        }

        Spacer(modifier = Modifier.height(16.dp))

//        DatePicker(
//            selectedDate = dueDate,
//            onDateSelected = { selectedDate ->
//                dueDate = selectedDate
//            },
//            text = { Text("Due Date") }, // Provide a label for the DatePicker
//            modifier = Modifier.fillMaxWidth()
//        )


        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown list for notification duration
        // Dropdown list for notification duration
        DropdownMenu(
            expanded = false, // Change to true when clicked
            onDismissRequest = { /* Dismiss the dropdown menu */ },
            modifier = Modifier.fillMaxWidth(),
        ) {
//            DropdownMenuItem(onClick = {
//                selectedDuration = "1 day before"
//            }) {
//                Text("1 day before")
//            }
//            DropdownMenuItem(onClick = {
//                selectedDuration = "2 days before"
//            }) {
//                Text("2 days before")
//            }
            // Add more options as needed
        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Save bill details to database or perform necessary actions
            // Reset fields after saving if needed
            title = ""
            category = ""
            amount = ""
            dueDate = null
            selectedDuration = ""
            // Hide keyboard after clicking the button
            keyboardController?.hide()
        },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) ,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray,
                contentColor = Color.White)) {
            Text("Add Bill")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun AddBillsPreview() {
    AddBills(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        navController = rememberNavController()
    )
}