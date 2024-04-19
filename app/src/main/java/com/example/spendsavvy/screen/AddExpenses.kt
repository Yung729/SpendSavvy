package com.example.spendsavvy.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.ui.theme.poppinsFontFamily
import com.example.spendsavvy.viewModels.CategoryViewModel
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpensesScreen(modifier: Modifier = Modifier, navController: NavController) {

    val catViewModel = CategoryViewModel()

    Column(
        modifier = modifier
    ) {

        var isExpanded by remember {
            mutableStateOf(false)
        }

        var selectedCategory by remember {
            mutableStateOf("")
        }

        var isExpanded1 by remember {
            mutableStateOf(false)
        }

        var selectedMethod by remember {
            mutableStateOf("")
        }

        var amount by remember {
            mutableStateOf("")
        }

        var description by remember {
            mutableStateOf("")
        }

        val expenseList by catViewModel.expensesList.collectAsState()

        val calendarState = rememberSheetState()
        val selectedDate = remember { mutableStateOf("") }

        CalendarDialog(
            state = calendarState,
            config = CalendarConfig(
                monthSelection = true,
                yearSelection = true,
                style = CalendarStyle.MONTH
            ),
            selection = CalendarSelection.Date{ date ->
                selectedDate.value = date.toString()
            })


        OutlinedButton(onClick = { calendarState.show()}) {
            Text(text = selectedDate.value.ifEmpty { "Select Date" })
        }
        
        Spacer(modifier = Modifier.height(30.dp))


        Text(
            text = "Category",
            fontFamily = poppinsFontFamily,
            fontSize = 15.sp
        )

        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = it }
        ) {
            TextField(
                value = selectedCategory,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    TrailingIcon(expanded = isExpanded)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                for (data in expenseList) {
                    DropdownMenuItem(
                        text = {
                            Text(text = data.categoryName)
                        },
                        onClick = {
                            selectedCategory = data.categoryName
                            isExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Amount",
            fontFamily = poppinsFontFamily,
            fontSize = 15.sp
        )

        TextField(
            value = amount,
            onValueChange = {
                amount = it
            },
            placeholder = {
                Text(
                    text = "RM 0",
                    fontFamily = poppinsFontFamily,
                    fontSize = 15.sp,
                    color = Color.Gray
                )
            }
        )

        Spacer(modifier = Modifier.height(30.dp))



        Text(
            text = "Payment Method",
            fontFamily = poppinsFontFamily,
            fontSize = 15.sp
        )

        ExposedDropdownMenuBox(
            expanded = isExpanded1,
            onExpandedChange = { isExpanded1 = it }
        ) {
            TextField(
                value = selectedMethod,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    TrailingIcon(expanded = isExpanded1)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = isExpanded1,
                onDismissRequest = { isExpanded1 = false }
            ) {
                for (data in expenseList) {
                    DropdownMenuItem(
                        text = {
                            Text(text = data.categoryName)
                        },
                        onClick = {
                            selectedMethod = data.categoryName
                            isExpanded1 = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Description",
            fontFamily = poppinsFontFamily,
            fontSize = 15.sp
        )

        TextField(
            value = description,
            onValueChange = {
                description = it
            },
            placeholder = {
                Text(
                    text = "Comment",
                    fontFamily = poppinsFontFamily,
                    fontSize = 15.sp,
                    color = Color.Gray
                )
            }
        )

    }
}

@Preview(showBackground = true)
@Composable
fun AddExpensesPreview() {
    AddExpensesScreen(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        navController = rememberNavController()
    )
}