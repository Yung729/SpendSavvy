package com.example.spendsavvy.screen.Analysis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.spendsavvy.components.bounceClick
import com.example.spendsavvy.viewModels.TargetViewModel
import java.time.YearMonth


@Composable
fun BudgetScreen(budgetViewModel: TargetViewModel) {


    var budgetAmountText by remember { mutableStateOf("0") }
    val budgetAmount: Double = budgetAmountText.toDoubleOrNull() ?: 0.0
    var goalAmountText by remember { mutableStateOf("0") }
    val goalAmount: Double = goalAmountText.toDoubleOrNull() ?: 0.0

    val budgetAmountFromDB = budgetViewModel.budget.observeAsState(initial = 0.0)
    val goalAmountFromDB = budgetViewModel.goal.observeAsState(initial = 0.0)

    // Get the current year and month
    val currentYearMonth = YearMonth.now()

    // Get the total days in the current month
    val totalDaysInMonth = currentYearMonth.lengthOfMonth()

    val monthlyBudgetAmount = budgetAmountFromDB.value * totalDaysInMonth
    val monthlyGoalAmount = goalAmountFromDB.value * totalDaysInMonth

    Column() {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Card(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    ), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()

                ) {
                    Column(
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Text(text = "Budget", fontWeight = FontWeight.Bold)

                        OutlinedTextField(
                            value = budgetAmountText,
                            onValueChange = { budgetAmountText = it },
                            label = { Text(text = "Daily Budget") },
                            maxLines = 1,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp, top = 10.dp),
                            shape = RoundedCornerShape(15.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        Text(text = "Daily Budget Amount = ${budgetAmountFromDB.value}")
                        Text(text = "Monthly Budget Amount = $monthlyBudgetAmount")

                        Button(
                            onClick = {
                                budgetViewModel.addBudget(amount = budgetAmount)
                            },
                            modifier = Modifier
                                .padding(bottom = 10.dp)
                                .bounceClick(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black
                            )
                        ) {
                            Text(
                                text = "Set",
                                textAlign = TextAlign.Center,
                                color = Color.White
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
                        Text(text = "Goal", fontWeight = FontWeight.Bold)

                        OutlinedTextField(
                            value = goalAmountText,
                            onValueChange = { goalAmountText = it },
                            label = { Text(text = "Daily Goal") },
                            maxLines = 1,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp, top = 10.dp),
                            shape = RoundedCornerShape(15.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        Text(text = "Daily Budget Amount = ${goalAmountFromDB.value}")
                        Text(text = "Monthly Budget Amount = $monthlyGoalAmount")

                        Button(
                            onClick = {
                                budgetViewModel.addGoal(amount = goalAmount)
                            },
                            modifier = Modifier
                                .padding(bottom = 10.dp)
                                .bounceClick(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black
                            )
                        ) {
                            Text(
                                text = "Set",
                                textAlign = TextAlign.Center,
                                color = Color.White
                            )
                        }
                    }
                }


            }
        }
    }
}
