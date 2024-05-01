package com.example.spendsavvy.screen

import android.widget.Toast
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.lifecycle.ViewModel
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

    val context = LocalContext.current
    var initialAmount by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var isMonthly by remember { mutableStateOf(false) }
    var isAnnually by remember { mutableStateOf(false) }
    var isSalaried by remember { mutableStateOf(false) }
    var isBusiness by remember { mutableStateOf(false) }

    fun validateAmount(amount: String): Boolean {
        return try {
            val value = amount.toDouble()
            if (value <= 0) {
                Toast.makeText(context, "Income cannot be negative and zero", Toast.LENGTH_SHORT).show()
                false
            } else {
                true
            }
        } catch (e: NumberFormatException) {
            Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show()
            false
        }
    }

    var income by remember { mutableStateOf(0) }
    var tax by remember { mutableStateOf(0) }
    var incomeAfterTax by remember { mutableStateOf(0) }

    fun calculateTax() {
        val selectedYear = selectedDate.year

        income = try {
            if (isMonthly) {
                initialAmount.toInt() * 12
            } else {
                initialAmount.toInt()
            }
        } catch (e: NumberFormatException) {
            println("Invalid initial amount: $initialAmount")
            return
        }

        tax = when {
            selectedYear == 2023 -> {
                when {
                    income in 0..5000 -> 0
                    income in 5001..20000 -> ((income - 5000) * 0.01).toInt()
                    income in 20001..35000 -> (150 + ((income - 20000) * 0.03)).toInt()
                    income in 35001..50000 -> (600 + ((income - 35000) * 0.06)).toInt()
                    income in 50001..70000 -> (1500 + ((income - 50000)) * 0.11).toInt()
                    income in 70001..100000 -> (3700 + ((income - 70000) * 0.19)).toInt()
                    income in 100001..400000 -> (9400 + ((income - 100000) * 0.25)).toInt()
                    income in 400001..600000 -> (84400 + ((income - 400000) * 0.26)).toInt()
                    income in 600001..2000000 -> (136400 + ((income - 600000) * 0.28)).toInt()
                    income >= 2000001 -> (528400 + ((income - 2000000) * 0.30)).toInt()
                    else -> 0
                }
            }
            selectedYear == 2022 || selectedYear == 2021-> {
                when {
                    income in 0..5000 -> 0
                    income in 5001..20000 -> ((income - 5000) * 0.01).toInt()
                    income in 20001..35000 -> (150 + ((income - 20000) * 0.03)).toInt()
                    income in 35001..50000 -> (600 + ((income - 35000) * 0.08)).toInt()
                    income in 50001..70000 -> (1800 + ((income - 50000)) * 0.13).toInt()
                    income in 70001..100000 -> (4400 + ((income - 70000) * 0.21)).toInt()
                    income in 100001..250000 -> (10700 + ((income - 100000) * 0.24)).toInt()
                    income in 250001..400000 -> (46700 + ((income - 250000) * 0.245)).toInt()
                    income in 400001..600000 -> (83450 + ((income - 400000) * 0.25)).toInt()
                    income in 600001..1000000 -> (133450 + ((income - 600000) * 0.26)).toInt()
                    income in 1000001..2000000 -> (237450 + ((income - 1000000) * 0.28)).toInt()
                    income >= 2000001 -> (517450 + ((income - 2000000) * 0.30)).toInt()
                    else -> 0
                }
            }
            selectedYear == 2020 -> {
                when {
                    income in 0..5000 -> 0
                    income in 5001..20000 -> ((income - 5000) * 0.01).toInt()
                    income in 20001..35000 -> (150 + ((income - 20000) * 0.03)).toInt()
                    income in 35001..50000 -> (600 + ((income - 35000) * 0.08)).toInt()
                    income in 50001..70000 -> (1800 + ((income - 50000)) * 0.13).toInt()
                    income in 70001..100000 -> (4600 + ((income - 70000) * 0.21)).toInt()
                    income in 100001..250000 -> (10900 + ((income - 100000) * 0.24)).toInt()
                    income in 250001..400000 -> (46900 + ((income - 250000) * 0.245)).toInt()
                    income in 400001..600000 -> (83650 + ((income - 400000) * 0.25)).toInt()
                    income in 600001..1000000 -> (133650 + ((income - 600000) * 0.26)).toInt()
                    income in 1000001..2000000 -> (237650 + ((income - 1000000) * 0.28)).toInt()
                    income >= 2000001 -> (517650 + ((income - 2000000) * 0.30)).toInt()
                    else -> 0
                }
            }
            selectedYear == 2019 || selectedYear == 2018 -> {
                when {
                    income in 0..5000 -> 0
                    income in 5001..20000 -> ((income - 5000) * 0.01).toInt()
                    income in 20001..35000 -> (150 + ((income - 20000) * 0.03)).toInt()
                    income in 35001..50000 -> (600 + ((income - 35000) * 0.08)).toInt()
                    income in 50001..70000 -> (1800 + ((income - 50000)) * 0.13).toInt()
                    income in 70001..100000 -> (4600 + ((income - 70000) * 0.21)).toInt()
                    income in 100001..250000 -> (10900 + ((income - 100000) * 0.24)).toInt()
                    income in 250001..400000 -> (46900 + ((income - 250000) * 0.245)).toInt()
                    income in 400001..600000 -> (83650 + ((income - 400000) * 0.25)).toInt()
                    income in 600001..1000000 -> (133650 + ((income - 600000) * 0.26)).toInt()
                    income >= 1000001 -> (237650 + ((income - 1000000) * 0.28)).toInt()
                    else -> 0
                }
            }
            selectedYear == 2017 || selectedYear == 2016 -> {
                when {
                    income in 0..5000 -> 0
                    income in 5001..20000 -> ((income - 5000) * 0.01).toInt()
                    income in 20001..35000 -> (150 + ((income - 20000) * 0.05)).toInt()
                    income in 35001..50000 -> (750 + ((income - 35000) * 0.10)).toInt()
                    income in 50001..70000 -> (1500 + ((income - 50000)) * 0.16).toInt()
                    income in 70001..100000 -> (3200 + ((income - 70000) * 0.21)).toInt()
                    income in 100001..250000 -> (6300 + ((income - 100000) * 0.24)).toInt()
                    income in 250001..400000 -> (11900 + ((income - 250000) * 0.245)).toInt()
                    income in 400001..600000 -> (37900 + ((income - 400000) * 0.25)).toInt()
                    income in 600001..1000000 -> (84650 + ((income - 600000) * 0.26)).toInt()
                    income >= 1000001 -> (138650 + ((income - 1000000) * 0.28)).toInt()
                    else -> 0
                }
            }
            selectedYear == 2015 -> {
                when {
                    income in 0..2500 -> 0
                    income in 2501..5000 -> 0
                    income in 5001..10000 -> ((income - 5000) * 0.01).toInt()
                    income in 10001..20000 -> (50 + ((income - 10000) * 0.01)).toInt()
                    income in 20001..35000 -> (150 + ((income - 20000) * 0.05)).toInt()
                    income in 35001..50000 -> (750 + ((income - 35000) * 0.10)).toInt()
                    income in 50001..70000 -> (3200 + ((income - 50000)) * 0.16).toInt()
                    income in 70001..100000 -> (6300 + ((income - 70000) * 0.21)).toInt()
                    income in 100001..150000 -> (12300 + ((income - 100000) * 0.24)).toInt()
                    income in 150001..250000 -> (24000 + ((income - 150000) * 0.24)).toInt()
                    income in 250001..400000 -> (47900 + ((income - 250000) * 0.245)).toInt()
                    income >= 400001 -> (84650 + ((income - 400000) * 0.25)).toInt()
                    else -> 0
                }
            }
            selectedYear == 2013 || selectedYear == 2014 -> {
                when {
                    income in 0..2500 -> 0
                    income in 2501..5000 -> 0
                    income in 5001..10000 -> ((income - 5000) * 0.02).toInt()
                    income in 10001..20000 -> (100 + ((income - 10000) * 0.02)).toInt()
                    income in 20001..35000 -> (300 + ((income - 20000) * 0.06)).toInt()
                    income in 35001..50000 -> (1200 + ((income - 35000) * 0.11)).toInt()
                    income in 50001..70000 -> (2850 + ((income - 50000)) * 0.19).toInt()
                    income in 70001..100000 -> (6650 + ((income - 70000) * 0.24)).toInt()
                    income >= 100001 -> (13850 + ((income - 100000) * 0.26)).toInt()
                    else -> 0
                }

            }
            else -> 0
        }

        incomeAfterTax = income - tax

        println("Initial Amount: $initialAmount")
        println("Selected Year: $selectedYear")
        println("Is Monthly: $isMonthly")
        println("Is Annually: $isAnnually")
        println("Is Salaried: $isSalaried")
        println("Is Business: $isBusiness")
        println("Income: $income")
        println("Tax: $tax")
        println("Income after Tax: $incomeAfterTax")
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
                            modifier = Modifier.size(45.dp)
                        )
                    }
                    Box(modifier = Modifier.weight(2f)) {
                        TextField(
                            value = initialAmount,
                            onValueChange = {
                                initialAmount = it
                                validateAmount(it) },
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
                val selectedIncomePeriod = radioButtonsIncomePeriod()
                isMonthly = selectedIncomePeriod == "Monthly"
                isAnnually = selectedIncomePeriod == "Annually"

                // Job Type
                val selectedJobType = radioButtonsJobType()
                isSalaried = selectedJobType == "Salaried"
                isBusiness = selectedJobType == "Business"

                LineDivider()

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
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Income",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                            Text(
                                text = "RM$income" ,
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
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                            Text(
                                text = "RM$income" ,
                                fontSize = 16.sp,
                            )
                            LineDivider()
                        }
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
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Tax",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                            Text(
                                text = "RM$tax" ,
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
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                            Text(
                                text = "RM$tax",
                                fontSize = 16.sp
                            )
                            LineDivider()
                        }
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
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Income after Tax",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                            Text(
                                text = "RM$incomeAfterTax",
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
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                            Text(
                                text = "RM$incomeAfterTax",
                                fontSize = 16.sp
                            )
                            LineDivider()
                        }
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
                    onClick = { calculateTax() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp),
                ) {
                    Text(text = "Calculate")
                }
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                    onClick = { /*TODO*/ },
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
fun radioButtonsIncomePeriod(): String {
    val options = listOf("Monthly", "Annually")
    var selectedOption by remember { mutableStateOf(options.first()) }

    RadioButtonGroup(
        label = "Income Period",
        icon = R.drawable.period_icon,
        options = options,
        onOptionSelected = { selectedOption = it }
    )

    return selectedOption
}

@Composable
fun radioButtonsJobType(): String {
    val options = listOf("Salaried", "Business")
    var selectedOption by remember { mutableStateOf(options.first()) }

    RadioButtonGroup(
        label = "Job Type",
        icon = R.drawable.job_icon,
        options = options,
        onOptionSelected = { selectedOption = it }
    )

    return selectedOption
}

@Composable
fun RadioButtonGroup(
    label: String,
    icon: Int,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
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
                    text = label,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = "Icon",
                    modifier = Modifier.size(45.dp)
                )
            }

            // Radio buttons
            Column(
                modifier = Modifier.weight(1f)
            ) {
                val radioButtons = remember {
                    mutableStateListOf<Boolean>().apply {
                        repeat(options.size) {
                            add(false)
                        }
                    }
                }

                options.forEachIndexed { index, option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                radioButtons.indices.forEach { i ->
                                    radioButtons[i] = i == index
                                }
                                onOptionSelected(option)
                            }
                    ) {
                        RadioButton(
                            colors = RadioButtonDefaults.colors(selectedColor = Color.Black),
                            selected = radioButtons[index],
                            onClick = {
                                radioButtons.indices.forEach { i ->
                                    radioButtons[i] = i == index
                                }
                                onOptionSelected(option)
                            }
                        )
                        Text(text = option)
                    }
                }
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
){
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
//        taxViewModel = TaxViewModel()
    )
}


