package com.example.spendsavvy.screen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.components.bounceClick


@Composable
fun AnalysisScreen(modifier: Modifier = Modifier, navController: NavController) {
    val scrollState = rememberScrollState()
    val openPopUp = remember { mutableStateOf(false) }

    Column(
        modifier = modifier.verticalScroll(state = scrollState)
    ) {
        Button(
            onClick = { },
            modifier = Modifier
                .padding(bottom = 10.dp)
                .bounceClick()
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
            )
        ) {
            Text(
                text = "Set Budget", textAlign = TextAlign.Center, color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        PieChart(
            data = mapOf(
                Pair("Expenses-1", 150),
                Pair("Expenses-2", 120),
                Pair("Sample-3", 110),
                Pair("Sample-4", 170),
                Pair("Sample-5", 120),
            )
        )

        if (openPopUp.value) {
            BudgetAddScreen(onDismissRequest = { openPopUp.value = false })
        }
    }
}

@Composable
fun BudgetAddScreen(
    onDismissRequest: () -> Unit
) {


    var budgetAmountText by remember { mutableStateOf("0") }
    var budgetAmount: Double = budgetAmountText.toDoubleOrNull() ?: 0.0


    Dialog(
        onDismissRequest = {
            onDismissRequest()
        }, properties = DialogProperties(
            usePlatformDefaultWidth = false, dismissOnBackPress = true
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { onDismissRequest() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back"
                    )
                }
            }

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





                Button(
                    onClick = {

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

@Preview(showBackground = true)
@Composable
fun AnalysisScreenPreview() {
    AnalysisScreen(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp), rememberNavController()
    )
}