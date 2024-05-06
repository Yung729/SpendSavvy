package com.example.spendsavvy.screen

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.R
import com.example.spendsavvy.navigation.Screen
import com.example.spendsavvy.viewModels.TaxViewModel
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.time.LocalDate

@Composable
fun TaxCalculator(
    modifier: Modifier = Modifier,
    navController: NavController,
    taxViewModel: TaxViewModel
) {
    val context = LocalContext.current
    var initialAmount by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var isMonthly by remember { mutableStateOf(false) }
    var isAnnually by remember { mutableStateOf(false) }

    val income by taxViewModel.income.observeAsState(initial = 0)
    val tax by taxViewModel.tax.observeAsState(initial = 0)
    val incomeAfterTax by taxViewModel.incomeAfterTax.observeAsState(initial = 0)

    fun validateAmount(amount: String): Boolean {
        return try {
            val value = amount.toDouble()
            if (value <= 0) {
                Toast.makeText(context, "Income cannot be negative and zero", Toast.LENGTH_SHORT)
                    .show()
                false
            } else {
                true
            }
        } catch (e: NumberFormatException) {
            Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show()
            false
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        item {
            Column {
                // Initial Amount
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Amount",
                            fontSize = 20.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.income_icon),
                            contentDescription = "Income Icon",
                            modifier = Modifier
                                .size(45.dp)
                                .align(CenterHorizontally)
                        )
                    }
                    Box(modifier = Modifier.weight(2f)) {
                        TextField(
                            value = initialAmount,
                            onValueChange = {
                                initialAmount = it
                                validateAmount(it)
                            },
                            modifier = Modifier.height(50.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                            placeholder = { Text(text = "Enter your income") }
                        )
                    }
                }

                // DatePickerItem
                DatePickerItem(
                    label = "Tax Year",
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it }
                )

                // Income Period
                RadioButtonsIncomePeriod { selectedOption ->
                    isMonthly = selectedOption == "Monthly"
                    isAnnually = selectedOption == "Annually"
                }

                LineDivider()

                // Display Income, Tax, and Income after Tax
                DisplayIncomeTax(
                    income = income,
                    tax = tax,
                    incomeAfterTax = incomeAfterTax,
                    isMonthly = isMonthly,
                    isAnnually = isAnnually
                )
            }
        }

        // Button Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                    onClick = {
                        if (!isMonthly && !isAnnually) {
                            Toast.makeText(
                                context,
                                "Please select one of the income period",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            taxViewModel.calculateTax(
                                initialAmount,
                                selectedDate,
                                isMonthly,
                                isAnnually
                            )
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp),
                ) {
                    Text(text = "Calculate")
                }
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                    onClick = { navController.navigate(Screen.AddExpenses.route) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp),
                ) {
                    Text(text = "Add in Expense", fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun RadioButtonsIncomePeriod(onOptionSelected: (String) -> Unit) {
    var selectedOption by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon and label text
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Income Period",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.period_icon),
                    contentDescription = "Icon",
                    modifier = Modifier
                        .size(60.dp)
                        .align(CenterHorizontally)
                        .padding(end = 5.dp)
                )
            }

            // Radio buttons
            Column(
                modifier = Modifier.weight(1f)
            ) {
                listOf("Monthly", "Annually").forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                selectedOption = option
                                onOptionSelected(selectedOption) // Invoke callback with selected option
                            }
                    ) {
                        RadioButton(
                            colors = RadioButtonDefaults.colors(selectedColor = Color.Black),
                            selected = option == selectedOption,
                            onClick = {
                                selectedOption = option
                                onOptionSelected(selectedOption) // Invoke callback with selected option
                            }
                        )
                        Text(
                            text = option,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DisplayIncomeTax(
    income: Int,
    tax: Int,
    incomeAfterTax: Int,
    isMonthly: Boolean,
    isAnnually: Boolean
) {
    Column {
        Row(
            modifier = Modifier
                .padding(top = 4.dp, start = 10.dp, end = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = "Monthly",
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Annually",
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Row(
            modifier = Modifier
                .padding(top = 4.dp, start = 10.dp, end = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = CenterHorizontally
            ) {
                Text(
                    text = "Income",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = if (isAnnually) "RM${income / 12}" else "RM$income",
                    fontSize = 16.sp,
                )

                LineDivider()
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = CenterHorizontally
            ) {
                Text(
                    text = "Income",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = if (isMonthly) "RM${income * 12}" else "RM$income",
                    fontSize = 16.sp,
                )
                LineDivider()
            }
        }

        // Display Tax
        Row(
            modifier = Modifier
                .padding(top = 4.dp, start = 10.dp, end = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = CenterHorizontally
            ) {
                Text(
                    text = "Tax",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = if (isAnnually) "RM${tax / 12}" else "RM$tax",
                    fontSize = 16.sp,
                )
                LineDivider()
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = CenterHorizontally
            ) {
                Text(
                    text = "Tax",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = if (isMonthly) "RM${tax * 12}" else "RM$tax",
                    fontSize = 16.sp
                )
                LineDivider()
            }
        }

        // Display Income after Tax
        Row(
            modifier = Modifier
                .padding(top = 4.dp, start = 10.dp, end = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = CenterHorizontally
            ) {
                Text(
                    text = "Income after Tax",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = if (isAnnually) "RM${incomeAfterTax / 12}" else "RM$incomeAfterTax",
                    fontSize = 16.sp
                )
                LineDivider()
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = CenterHorizontally
            ) {
                Text(
                    text = "Income after Tax",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = if (isMonthly) "RM${incomeAfterTax * 12}" else "RM$incomeAfterTax",
                    fontSize = 16.sp
                )
                LineDivider()
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerItem(
    label: String,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val calendarState = rememberSheetState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Label text and icon
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.date_icon),
                contentDescription = "Date Picker Icon",
                modifier = Modifier
                    .size(35.dp)
                    .align(CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.width(20.dp))

        Box(
            modifier = Modifier.weight(2f)
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize(Alignment.Center)
                    .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(4.dp))
                    .padding(4.dp)
                    .clickable { calendarState.show() }
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = selectedDate.toString(),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }

    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
            style = CalendarStyle.MONTH,
            disabledDates = listOf(LocalDate.now().plusDays(7))
        ),
        selection = CalendarSelection.Date { date ->
            onDateSelected(date)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun TaxCalculatorPreview() {
    TaxCalculator(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        navController = rememberNavController(),
        taxViewModel = TaxViewModel()
    )
}
