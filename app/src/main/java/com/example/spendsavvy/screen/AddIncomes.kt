package com.example.spendsavvy.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.components.bounceClick
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.ui.theme.poppinsFontFamily
import com.example.spendsavvy.viewModels.CategoryViewModel
import com.example.spendsavvy.viewModels.OverviewViewModel
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIncomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    transactionViewModel: OverviewViewModel = viewModel()
) {

    val catViewModel = CategoryViewModel()

    Column(
        modifier = modifier
    ) {

        var isExpanded by remember {
            mutableStateOf(false)
        }

        var selectedCategory by remember {
            mutableStateOf(Category())
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

        val incomeList by catViewModel.incomeList.observeAsState(initial = emptyList())

        val todayDate = Date()
        val calendarState = rememberSheetState()
        val selectedDate = remember { mutableStateOf<Date>(todayDate) }
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        CalendarDialog(
            state = calendarState,
            config = CalendarConfig(
                monthSelection = true,
                yearSelection = true,
                style = CalendarStyle.MONTH
            ),
            selection = CalendarSelection.Date { date ->
                val selectedDateValue =
                    Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant())
                selectedDate.value = selectedDateValue
            })


        OutlinedButton(onClick = { calendarState.show() }) {
            Text(text = selectedDate.value.let { dateFormat.format(it) } ?: "Select Date")
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
                value = selectedCategory.categoryName,
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
                for (data in incomeList) {
                    DropdownMenuItem(
                        text = {
                            Text(text = data.categoryName)
                        },
                        onClick = {
                            selectedCategory = data
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
                for (data in incomeList) {
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

        Button(
            onClick = {
                // If all fields have data, add the category
                transactionViewModel.addTransactionToFirestore(
                    Transactions(
                        amount = amount.toDoubleOrNull() ?: 0.0,
                        description = description,
                        date = selectedDate.value,
                        category = selectedCategory,
                        transactionType = "Incomes"
                    )
                )

            },
            modifier = Modifier
                .padding(bottom = 10.dp)
                .bounceClick(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            )
        ) {
            Text(
                text = "Add",
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }


    }
}

@Preview(showBackground = true)
@Composable
fun AddIncomePreview() {
    AddIncomeScreen(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        navController = rememberNavController()
    )
}