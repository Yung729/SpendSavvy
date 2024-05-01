package com.example.spendsavvy.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.spendsavvy.components.bounceClick
import com.example.spendsavvy.viewModels.BudgetViewModel


@Composable
fun BudgetScreen(budgetViewModel: BudgetViewModel) {


    var budgetAmountText by remember { mutableStateOf("0") }
    val budgetAmount: Double = budgetAmountText.toDoubleOrNull() ?: 0.0

    val budgetAmountFromDB = budgetViewModel.budget.observeAsState(initial = 0.0)

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


                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = budgetAmountText,
                    onValueChange = { budgetAmountText = it },
                    label = { Text(text = "Monthly Budget") },
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp, top = 10.dp),
                    shape = RoundedCornerShape(15.dp),
                    singleLine = true,
                )

                Text(text = "Budget Amount = ${budgetAmountFromDB.value}")

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
    }
}