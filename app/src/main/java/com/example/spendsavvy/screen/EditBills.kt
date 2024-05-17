package com.example.spendsavvy.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spendsavvy.R
import com.example.spendsavvy.models.Bills
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.ui.theme.poppinsFontFamily
import com.example.spendsavvy.viewModels.BillsViewModel
import com.example.spendsavvy.viewModels.CategoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBills(
    modifier: Modifier = Modifier,
    navController: NavController,
    bill: Bills,
    billsViewModel: BillsViewModel,
    catViewModel: CategoryViewModel
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    val expenseList by catViewModel.expensesList.observeAsState(initial = emptyList())
    var isExpanded by remember { mutableStateOf(false) }

    var updatedDescription by remember { mutableStateOf(bill.description) }
    var updatedAmount by remember { mutableStateOf(bill.amount.toString()) }
    var updatedDueDate by remember { mutableStateOf(bill.selectedDueDate) }
    var updatedDuration by remember { mutableStateOf(bill.selectedDuration) }
    var updatedCategory by remember { mutableStateOf(bill.category) }

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
            Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show()
            false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.text_23),
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, end = 18.dp, bottom = 18.dp)
        )
        OutlinedTextField(
            value = updatedDescription,
            onValueChange = { updatedDescription = it },
            label = { Text(stringResource(id = R.string.description)) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = updatedAmount,
            onValueChange = { updatedAmount = it },
            label = { Text(stringResource(id = R.string.amount)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(text = stringResource(id = R.string.category), fontFamily = poppinsFontFamily, fontSize = 15.sp)

            ExposedDropdownMenuBox(expanded = isExpanded,
                onExpandedChange = { isExpanded = it }) {
                TextField(
                    value = updatedCategory.categoryName,
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
                            updatedCategory = data
                            isExpanded = false
                        })
                    }
                }
            }
        }
        DueDatePicker(
            label = stringResource(id = R.string.dueDate),
            selectedDueDate = updatedDueDate,
            onDateSelected = { updatedDueDate = it }
        )
        Spacer(modifier = Modifier.height(12.dp))

        // DropDown for Status
        DropDown(
            label = stringResource(id = R.string.setReminder),
            selectedDuration = updatedDuration,
            onDurationSelected = { newDuration ->
                updatedDuration = newDuration
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (updatedDescription.isNotBlank() && updatedAmount.isNotBlank() && updatedCategory != Category() && updatedDuration.isNotBlank() && validateAmount(updatedAmount)) {
                    showDialog = true
                } else {
                    Toast.makeText(
                        context,
                        "Please make sure to fill in all the required fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray,
                contentColor = Color.White
            )
        ) {
            Text(stringResource(id = R.string.saveChanges))
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text(
                        text = stringResource(id = R.string.text_24),
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
                            onClick = { showDialog = false },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Red,
                            )
                        ) {
                            Text(text =stringResource(id = R.string.cancel))
                        }

                        Button(
                            onClick = {
                                billsViewModel.editBill(
                                    bills = bill,
                                    updatedBills = Bills(
                                        id = bill.id,
                                        amount = updatedAmount.toDoubleOrNull() ?: 0.0,
                                        category = updatedCategory,
                                        description = updatedDescription,
                                        selectedDueDate = updatedDueDate,
                                        selectedDuration = updatedDuration,
                                        billsStatus = bill.billsStatus
                                    )
                                )
                                showDialog = false
                                navController.navigateUp()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.DarkGray,
                                contentColor = Color.White
                            )
                        ) {
                            Text(stringResource(id = R.string.saveChanges))
                        }
                    }
                }
            )
        }
    }
}

