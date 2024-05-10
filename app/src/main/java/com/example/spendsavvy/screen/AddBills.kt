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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.models.Bills
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.ui.theme.poppinsFontFamily
import com.example.spendsavvy.viewModels.BillsViewModel
import com.example.spendsavvy.viewModels.CategoryViewModel
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import kotlinx.coroutines.selects.select
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBills(modifier: Modifier = Modifier, navController: NavController, billsViewModel: BillsViewModel, catViewModel: CategoryViewModel ) {

    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(Category()) }
    var amount by remember { mutableStateOf("") }
    var selectedDueDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedDuration by remember { mutableStateOf("") }
    val billsStatus by remember { mutableStateOf("UPCOMING") }  //default == upcoming

    val expenseList by catViewModel.expensesList.observeAsState(initial = emptyList())
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
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
                .padding(start = 18.dp, end = 18.dp, top = 10.dp, bottom = 18.dp)
        )

        Text(
            text = "Description",
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
        )
        OutlinedTextFieldItem(
            label = "Description",
            placeholder = "Description",
            value = description,
            keyboardType = KeyboardType.Text,
            onValueChange = { description = it },
            height = 60.dp
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text( text = "Category", fontFamily = poppinsFontFamily, fontSize = 15.sp)

                ExposedDropdownMenuBox(expanded = isExpanded,
                    onExpandedChange = { isExpanded = it }) {
                    TextField(value = selectedCategory.categoryName,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(expanded = isExpanded,
                        onDismissRequest = { isExpanded = false }) {
                        for (data in expenseList) {
                            DropdownMenuItem(text = {
                                Text(text = data.categoryName)
                            }, onClick = {
                                selectedCategory = data
                                isExpanded = false
                            })
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Amount",
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
        )
        OutlinedTextFieldItem(
            label = "Amount",
            placeholder = "Amount",
            value = amount,
            keyboardType = KeyboardType.Number,
            onValueChange = { amount = it},
            height = 60.dp
        )

        DueDatePicker(
            label = "Due Date",
            selectedDueDate = selectedDueDate,
            onDateSelected = { selectedDueDate = it },
        )
        DropDown(
            label = "Set Reminder before due date",
            selectedDuration = selectedDuration,
            onDurationSelected = { selectedDuration = it }
        )

        Spacer(modifier = Modifier.height(5.dp))

        Button(
            onClick = {
                if (description.isNotBlank() && amount.isNotBlank() && selectedCategory != Category() && selectedDueDate != null && selectedDuration.isNotBlank()) {
                    showDialog = true
                }
                else{
                    Toast.makeText(
                        context,
                        "Please make sure to fill in all the required fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray,
                contentColor = Color.White
            )
        ) {
            Text("Add Bill")
        }

        if(showDialog){
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text(
                        text = "Confirm to add bill?",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedButton(
                            onClick = {
                                showDialog = false
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Red,
                            )
                        ) {
                            Text(text = "Cancel")
                        }

                        Button(
                            onClick = {
                                billsViewModel.addBillsToFirestore(
                                    Bills(
                                        id = billsViewModel.generateBillId(),
                                        amount = amount.toDoubleOrNull() ?: 0.0,
                                        description = description,
                                        category = selectedCategory,
                                        selectedDueDate = selectedDueDate.toString(),
                                        selectedDuration = selectedDuration,
                                        billsStatus = billsStatus
                                    ),
                                    onSuccess = {
                                        Toast.makeText(
                                            context,
                                            "Bill added successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                ) {
                                    Toast.makeText(
                                        context,
                                        "Failed to add Bills",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                showDialog = false
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.DarkGray,
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = "OK")
                        }
                    }
                }
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDown(label: String, selectedDuration: String, onDurationSelected: (String) -> Unit) {
    val list = listOf("1 day before", "2 days before", "1 week before", "2 weeks before", "1 month before")

    var expandedState by remember { mutableStateOf(false) }
    var currentSelectedDuration by remember { mutableStateOf(selectedDuration) }

    Column(
        modifier = Modifier
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = label, fontSize = 20.sp)
        ExposedDropdownMenuBox(
            expanded = expandedState,
            onExpandedChange = { expandedState = it }
        ) {
            TextField(
                modifier = Modifier.menuAnchor(),
                value = currentSelectedDuration,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedState) }
            )

            ExposedDropdownMenu(expanded = expandedState, onDismissRequest = { expandedState = false }) {
                list.forEachIndexed { index, text ->
                    DropdownMenuItem(
                        text = { Text(text = text) },
                        onClick = {
                            currentSelectedDuration = list[index]
                            onDurationSelected(list[index])
                            expandedState = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DueDatePicker(
    label: String,
    selectedDueDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val calendarState = rememberSheetState()
    val currentYear = LocalDate.now().year
    val currentDate = LocalDate.now()
    val disabledDates = (Month.JANUARY.value..currentDate.month.value).flatMap { month ->
        val lastDayOfMonth = if (currentYear == currentDate.year && month == currentDate.month.value)
            currentDate.dayOfMonth - 1
        else
            YearMonth.of(currentYear, month).lengthOfMonth()
        (1..lastDayOfMonth).map { day ->
            LocalDate.of(currentYear, month, day)
        }
    }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = label, fontSize = 20.sp)
        Box(
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(4.dp))
                .padding(4.dp)
                .clickable { calendarState.show() }
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = selectedDueDate.toString(),
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
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
                if (disabledDates.contains(date)) {
                    Toast.makeText(
                        context,
                        "Please select a valid due date",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    onDateSelected(date)
                }
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun AddBillsPreview() {
    AddBills(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        navController = rememberNavController(),
        billsViewModel = viewModel(),
        catViewModel = viewModel()
    )
}