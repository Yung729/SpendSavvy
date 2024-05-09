package com.example.spendsavvy.screen.Overview

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.R
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
    transactionViewModel: OverviewViewModel,
    catViewModel: CategoryViewModel
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
    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    val context = LocalContext.current

    //validation
    var isCategoryValid by remember { mutableStateOf(false) }
    var isAmountValid by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {


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

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ), shape = RoundedCornerShape(16.dp), modifier = Modifier
                .fillMaxWidth()

        ) {
            Column(
                modifier = Modifier.padding(10.dp)

            ) {
                Text(
                    text = "Date", fontFamily = poppinsFontFamily, fontSize = 15.sp
                )

                Row(
                    modifier = Modifier.clickable(onClick = { calendarState.show() })
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.calendar),
                        contentDescription = "Calendar",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = dateFormat.format(selectedDate.value),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(30.dp))


        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()

        ) {
            Column(
                modifier = Modifier.padding(10.dp)

            ) {

                Text(
                    text = "Category", fontFamily = poppinsFontFamily, fontSize = 15.sp
                )

                ExposedDropdownMenuBox(expanded = isExpanded,
                    onExpandedChange = { isExpanded = it }) {
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

                    ExposedDropdownMenu(expanded = isExpanded,
                        onDismissRequest = { isExpanded = false }) {
                        for (data in incomeList) {
                            DropdownMenuItem(text = {
                                Text(text = data.categoryName)
                            }, onClick = {
                                selectedCategory = data
                                isExpanded = false
                                isCategoryValid = true
                            })
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()

        ) {
            Column(
                modifier = Modifier.padding(10.dp)

            ) {

                TextField(value = amount,
                    onValueChange = {
                        amount = it
                        isAmountValid = it.toDoubleOrNull() != null
                    },
                    placeholder = {
                        Text(
                            text = "RM 0",
                            fontFamily = poppinsFontFamily,
                            fontSize = 15.sp,
                            color = Color.Gray
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    maxLines = 1,
                    isError = !isAmountValid,
                    label = {
                        Text(
                            text = "Amount", fontFamily = poppinsFontFamily, fontSize = 15.sp
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))


        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()

        ) {

            Column(
                modifier = Modifier.padding(10.dp)

            ) {
                Text(
                    text = "Payment Method", fontFamily = poppinsFontFamily, fontSize = 15.sp
                )

                ExposedDropdownMenuBox(expanded = isExpanded1,
                    onExpandedChange = { isExpanded1 = it }) {
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

                    ExposedDropdownMenu(expanded = isExpanded1,
                        onDismissRequest = { isExpanded1 = false }) {
                        for (data in incomeList) {
                            DropdownMenuItem(text = {
                                Text(text = data.categoryName)
                            }, onClick = {
                                selectedMethod = data.categoryName
                                isExpanded1 = false
                            })
                        }
                    }
                }
            }

        }

        Spacer(modifier = Modifier.height(30.dp))

        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()

        ) {

            Column(
                modifier = Modifier.padding(10.dp)

            ) {

                TextField(value = description, onValueChange = {
                    description = it
                }, placeholder = {
                    Text(
                        text = "Comment",
                        fontFamily = poppinsFontFamily,
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                }, label = {
                    Text(
                        text = "Description", fontFamily = poppinsFontFamily, fontSize = 15.sp
                    )
                })
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {

                if (isAmountValid && isCategoryValid) {
                    transactionViewModel.addTransactionToFirestore(
                        Transactions(
                            id = transactionViewModel.generateTransactionId(),
                            amount = amount.toDoubleOrNull() ?: 0.0,
                            description = description,
                            date = selectedDate.value,
                            category = selectedCategory,
                            transactionType = "Incomes"
                        ),
                        onSuccess = {
                            Toast.makeText(
                                context,
                                "Incomes added successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onFailure = {
                            Toast.makeText(
                                context,
                                "Failed to add Incomes",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                } else {
                    if (!isAmountValid) {
                        Toast.makeText(
                            context,
                            "Amount is Empty",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (!isCategoryValid) {
                        Toast.makeText(
                            context,
                            "Category is Empty",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

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
        navController = rememberNavController(),
        transactionViewModel = viewModel(),
        catViewModel = viewModel()
    )
}