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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.res.stringResource
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
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBills(modifier: Modifier = Modifier, navController: NavController, billsViewModel: BillsViewModel, catViewModel: CategoryViewModel ) {

    val context = LocalContext.current

    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(Category()) }
    var amount by remember { mutableStateOf("") }
    var selectedDueDate by remember { mutableStateOf(Date()) }

    val selectedCalendar = Calendar.getInstance().apply {
        time = selectedDueDate
    }
    val currentDateMillis = Calendar.getInstance().timeInMillis
    val selectedCalendarMillis = selectedCalendar.timeInMillis

    var billsStatus by remember { mutableStateOf("") }

    billsStatus = if (selectedCalendarMillis < currentDateMillis) {
        "OVERDUE"
    } else {
        "UPCOMING"
    }

    val expenseList by catViewModel.expensesList.observeAsState(initial = emptyList())
    var isExpanded by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }

    fun validateAmount(amount: String): Boolean {
        return try {
            val value = amount.toDouble()
            if (value <= 0) {
                Toast.makeText(context, "Amount cannot be negative and zero", Toast.LENGTH_SHORT)
                    .show()
                false
            } else {
                true
            }
        } catch (e: NumberFormatException) {
            Toast.makeText(context, "Invalid Amount Input", Toast.LENGTH_SHORT).show()
            false
        }
    }
    Column(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(id = R.string.text_14),
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, end = 18.dp, top = 10.dp, bottom = 18.dp)
        )
        Text(
            text = stringResource(id = R.string.description),
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
        )
        OutlinedTextFieldItem(
            label = stringResource(id = R.string.description),
            placeholder = stringResource(id = R.string.description),
            value = description,
            keyboardType = KeyboardType.Text,
            onValueChange = { description = it },
            height = 70.dp,
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text( text = stringResource(id = R.string.category), fontFamily = poppinsFontFamily, fontSize = 15.sp)

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
            text = stringResource(id = R.string.amount),
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
        )
        OutlinedTextFieldItem(
            label = "Amount",
            placeholder = stringResource(id = R.string.amount),
            value = amount,
            keyboardType = KeyboardType.Number,
            onValueChange = { amount = it},
            height = 70.dp,
        )

        DueDatePicker(
            label = stringResource(id = R.string.dueDate),
            selectedDueDate = selectedDueDate,
            onDateSelected = { selectedDueDate = it },
        )


        Spacer(modifier = Modifier.height(5.dp))
        Button(
            onClick = {
                if (description.isNotBlank() && amount.isNotBlank() && selectedCategory != Category() && validateAmount(amount)) {
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
            Text(stringResource(id = R.string.addBill))
        }

        if(showDialog){
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text(
                        text = stringResource(id = R.string.text_28),
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
                            Text(text = stringResource(id = R.string.cancel))
                        }

                        Button(
                            onClick = {
                                billsViewModel.addBillsToFirestore(
                                    Bills(
                                        id = billsViewModel.generateBillId(),
                                        amount = amount.toDoubleOrNull() ?: 0.0,
                                        description = description,
                                        category = selectedCategory,
                                        selectedDueDate = selectedDueDate,
                                        billsStatus = billsStatus
                                    ),
                                    onSuccess = {
                                        Toast.makeText(
                                            context,
                                            "Bill added successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        navController.navigateUp()
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
                            Text(text = stringResource(id = R.string.add))
                        }
                    }
                }
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DueDatePicker(
    label: String,
    selectedDueDate: Date,
    onDateSelected: (Date) -> Unit
) {
    val calendarState = rememberSheetState()

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
                text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDueDate),
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
            ),
            selection = CalendarSelection.Date { date ->
                val selectedDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant())
                onDateSelected(selectedDate)
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