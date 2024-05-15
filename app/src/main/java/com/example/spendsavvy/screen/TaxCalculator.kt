package com.example.spendsavvy.screen

import android.net.Uri
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
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
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
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.viewModels.OverviewViewModel
import com.example.spendsavvy.viewModels.TaxViewModel
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.util.Date

@Composable
fun TaxCalculator(
    modifier: Modifier = Modifier,
    navController: NavController,
    transactionViewModel: OverviewViewModel
) {
    val taxViewModel = TaxViewModel()
    val context = LocalContext.current
    var initialAmount by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var isMonthly by remember { mutableStateOf(false) }
    var isAnnually by remember { mutableStateOf(false) }

    val incomeMonthly by taxViewModel.incomeMonthly.observeAsState(initial = 0.0)
    val taxMonthly by taxViewModel.taxMonthly.observeAsState(initial = 0.0)
    val incomeAfterTaxMonthly by taxViewModel.incomeAfterTaxMonthly.observeAsState(initial = 0.0)
    val incomeAnnually by taxViewModel.incomeAnnually.observeAsState(initial = 0.0)
    val taxAnnually by taxViewModel.taxAnnually.observeAsState(initial = 0.0)
    val incomeAfterTaxAnnually by taxViewModel.incomeAfterTaxAnnually.observeAsState(initial = 0.0)

    val tax :Double = taxAnnually
    val currentDate: Date = Date()
    var showDialog by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(true) }

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
                            text = stringResource(id = R.string.amount),
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
                            placeholder = { Text(text = stringResource(id = com.example.spendsavvy.R.string.text_15)) }
                        )
                    }
                }

                // DatePickerItem
                DatePickerItem(
                    label = stringResource(id = com.example.spendsavvy.R.string.taxYear),
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
                    incomeMonthly = incomeMonthly,
                    taxMonthly = taxMonthly,
                    incomeAfterTaxMonthly = incomeAfterTaxMonthly,
                    incomeAnnually = incomeAnnually,
                    taxAnnually = taxAnnually,
                    incomeAfterTaxAnnually = incomeAfterTaxAnnually,
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
                        when {
                            !isMonthly && !isAnnually -> {
                                Toast.makeText(
                                    context,
                                    "Please select one of the income periods",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            initialAmount.isBlank() -> {
                                Toast.makeText(
                                    context,
                                    "Please enter the initial amount",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            selectedDate.year == LocalDate.now().year -> {
                                Toast.makeText(
                                    context,
                                    "Please select a valid tax year",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            else -> {
                                taxViewModel.calculateTax(
                                    initialAmount,
                                    selectedDate,
                                    isMonthly,
                                    isAnnually
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp),
                ) {
                    Text(text = stringResource(id = com.example.spendsavvy.R.string.calculate))
                }
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                    onClick = {
                        if(tax != 0.0) {
                            transactionViewModel.addTransactionToFirestore(
                                Transactions(
                                    id = transactionViewModel.generateTransactionId(),
                                    amount = tax,
                                    description = "Income Tax of ${selectedDate.year}",
                                    date = currentDate,
                                    category = Category(
                                        id = "CT0009",
                                        imageUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/spendsavvy-5a2a8.appspot.com/o/images%2FincomeTax.png?alt=media&token=c4d11810-731f-41e0-a248-a921733754d2"),
                                        categoryName = "Income Tax",
                                        categoryType = "Expenses"
                                    ),
                                    paymentMethod = "Cash",
                                    transactionType = "Expenses"
                                ),
                                onSuccess = {
                                    showDialog = true
                                    success = true
                                    println("Income Tax added successfully")
                                },
                                onFailure = {
                                    showDialog = true
                                    success = false
                                    println("Failed to add income Tax")
                                }
                            )
                        }
                        else{
                            showDialog = true
                            success = false
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp),
                ) {
                    Text(text = stringResource(id = com.example.spendsavvy.R.string.addExpense), fontSize = 11.sp)
                }
                if (showDialog) {
                    TransactionResultDialog(
                        success = success,
                        message = if (success) stringResource(id = com.example.spendsavvy.R.string.text_26) else stringResource(id = com.example.spendsavvy.R.string.text_27),
                        onDismiss = { showDialog = false }
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionResultDialog(
    success: Boolean,
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = message,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                color = if (success) Color.Black else Color.Red,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = CenterHorizontally
            ) {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .padding(start = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = if (success) stringResource(id = com.example.spendsavvy.R.string.ctn) else stringResource(id = com.example.spendsavvy.R.string.tryAgain))
                }
            }
        }
    )
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
                    text = stringResource(id = com.example.spendsavvy.R.string.incomePeriod),
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
                                onOptionSelected(selectedOption)
                            }
                    ) {
                        RadioButton(
                            colors = RadioButtonDefaults.colors(selectedColor = Color.Black),
                            selected = option == selectedOption,
                            onClick = {
                                selectedOption = option
                                onOptionSelected(selectedOption)
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
    incomeMonthly: Double,
    taxMonthly: Double,
    incomeAfterTaxMonthly: Double,
    incomeAnnually: Double,
    taxAnnually: Double,
    incomeAfterTaxAnnually: Double,
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
                text = stringResource(id = com.example.spendsavvy.R.string.monthly),
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = stringResource(id = com.example.spendsavvy.R.string.annually),
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
                    text = stringResource(id = com.example.spendsavvy.R.string.income),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "RM %.2f".format(incomeMonthly),
                    fontSize = 16.sp,
                )

                LineDivider()
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = com.example.spendsavvy.R.string.income),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "RM %.2f".format(incomeAnnually),
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
                    text = stringResource(id = com.example.spendsavvy.R.string.tax),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "RM %.2f".format(taxMonthly),
                    fontSize = 16.sp,
                )
                LineDivider()
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = com.example.spendsavvy.R.string.tax),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "RM %.2f".format(taxAnnually),
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
                    text = stringResource(id = com.example.spendsavvy.R.string.incomeAfterTax),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "RM %.2f".format(incomeAfterTaxMonthly),
                    fontSize = 16.sp
                )
                LineDivider()
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = com.example.spendsavvy.R.string.incomeAfterTax),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "RM %.2f".format(incomeAfterTaxAnnually),
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
    val currentYear = LocalDate.now().year
    val disabledDates = (currentYear..currentYear).flatMap { year ->
        listOf(
            LocalDate.of(year, Month.JANUARY, 1),
            LocalDate.of(year, Month.DECEMBER, 31)
        )
    }
    val validYear = currentYear - 1
    val context = LocalContext.current

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
            )
            Text(
                text = "( 2013 - $validYear )",
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 6.dp)
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
            disabledDates = disabledDates
        ),
        selection = CalendarSelection.Date { date ->
            if (date.year == currentYear) {
                Toast.makeText(
                    context,
                    "Please select a valid tax year",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                onDateSelected(date)
            }
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
        transactionViewModel = viewModel()
    )
}
