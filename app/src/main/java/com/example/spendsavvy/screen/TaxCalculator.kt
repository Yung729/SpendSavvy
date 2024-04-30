package com.example.spendsavvy.screen

import android.util.Log
import android.widget.ToggleButton
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Checkbox
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.R
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.time.LocalDate

@Composable
fun TaxCalculator(modifier: Modifier = Modifier, navController: NavController) {
    var initialAmount by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var isMonthly by remember { mutableStateOf(true) }
    var isAnnually by remember { mutableStateOf(true) }
    var isSalaried by remember { mutableStateOf(true) }
    var isBusiness by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 20.dp)
    ) {
        item {
            Column {
                // Initial Amount
                Row(
                    modifier = Modifier.padding(10.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Initial Amount",
                            fontSize = 18.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.income_icon),
                            contentDescription = "Income Icon",
                            modifier = Modifier.size(45.dp)
                        )
                    }
                    Box(modifier = Modifier.weight(2f)) {
                        TextField(
                            value = initialAmount,
                            onValueChange = { initialAmount = it },
                            modifier = Modifier.height(50.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                            placeholder = { Text(text = "Enter amount") }
                        )
                    }
                }

                // DatePickerItem
                DatePickerItem(
                    label = "Tax Year",
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it }
                )

                // Income Period Row
                Row(
                    modifier = Modifier.padding(10.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Income Period",
                            fontSize = 18.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.period_icon),
                            contentDescription = "Time Icon",
                            modifier = Modifier.size(45.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        RadioButtonItem(
                            label = "Monthly",
                            selected = isMonthly,
                            onSelect = { isMonthly = true }
                        )
                        RadioButtonItem(
                            label = "Annually",
                            selected = isAnnually,
                            onSelect = { isMonthly = false }
                        )
                    }
                }

                // Job type Row
                Row(
                    modifier = Modifier.padding(10.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Job Type",
                            fontSize = 18.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.job_icon),
                            contentDescription = "Time Icon",
                            modifier = Modifier.size(45.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        RadioButtonItem(
                            label = "Salaried",
                            selected = isSalaried,
                            onSelect = { isSalaried = true }
                        )
                        RadioButtonItem(
                            label = "Business",
                            selected = isBusiness,
                            onSelect = { isSalaried = false }
                        )
                    }
                }

                LineDivider()
            }
        }

        item {
            // Monthly and Annually Row
            Row(
                modifier = Modifier
                    .padding(top = 4.dp, start = 10.dp, end = 10.dp) // Adjusted top padding
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
        }

        // Income Row
        item {
            Row(
                modifier = Modifier
                    .padding(top = 4.dp, start = 10.dp, end = 10.dp) // Adjusted top padding
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Income",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 2.dp) // Adjusted bottom padding
                    )
                    Text(
                        text = "RMXXXXX",
                        fontSize = 16.sp,
                    )
                    LineDivider()
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Income",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 2.dp) // Adjusted bottom padding
                    )
                    Text(
                        text = "RMXXXX",
                        fontSize = 16.sp,
                    )
                    LineDivider()
                }
            }
        }

        // Tax Row
        item {
            Row(
                modifier = Modifier
                    .padding(top = 4.dp, start = 10.dp, end = 10.dp) // Adjusted top padding
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Tax",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 2.dp) // Adjusted bottom padding
                    )
                    Text(
                        text = "RMXXXXX",
                        fontSize = 16.sp,
                    )
                    LineDivider()
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Tax",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 2.dp) // Adjusted bottom padding
                    )
                    Text(
                        text = "RMXXXX",
                        fontSize = 16.sp
                    )
                    LineDivider()
                }
            }
        }

        // Income After Tax Row
        item {
            Row(
                modifier = Modifier
                    .padding(top = 4.dp, start = 10.dp, end = 10.dp) // Adjusted top padding
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Income after Tax",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 2.dp) // Adjusted bottom padding
                    )
                    Text(
                        text = "RMXXXXX",
                        fontSize = 16.sp
                    )
                    LineDivider()
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Income after Tax",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 2.dp) // Adjusted bottom padding
                    )
                    Text(
                        text = "RMXXXX",
                        fontSize = 16.sp
                    )
                    LineDivider()
                }
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
                    onClick = { calculate() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp), // Adjusted padding
                ) {
                    Text(text = "Calculate")
                }
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp), // Adjusted padding
                ) {
                    Text(text = "Add in Expense", fontSize = 11.sp)
                }
            }
        }
    }
}


fun calculate(){

}

@Composable
fun RadioButtonItem(
    label: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(selectedColor = Color.Black),
            modifier = Modifier.padding(end = 4.dp)
        )
        Text(
            text = label,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
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
            )
        }

        Spacer(modifier = Modifier.width(20.dp))

        // Selected date box
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
        navController = rememberNavController()
    )
}