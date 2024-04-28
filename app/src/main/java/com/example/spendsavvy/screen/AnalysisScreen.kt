package com.example.spendsavvy.screen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.components.bounceClick
import com.example.spendsavvy.navigation.Screen
import com.example.spendsavvy.viewModels.OverviewViewModel
import kotlin.math.roundToInt


@Composable
fun AnalysisScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    transactionViewModel: OverviewViewModel
) {
    val scrollState = rememberScrollState()
    val expensesData by transactionViewModel.expensesTotal.observeAsState(initial = 0.0)
    val incomesData by transactionViewModel.incomesTotal.observeAsState(initial = 0.0)

    Column(
        modifier = modifier.verticalScroll(state = scrollState)
    ) {
        Button(
            onClick = { navController.navigate(Screen.BudgetScreen.route) },
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
                Pair("Expenses", expensesData.roundToInt()),
                Pair("Incomes", incomesData.roundToInt())
            )
        )


    }
}


@Preview(showBackground = true)
@Composable
fun AnalysisScreenPreview() {
    AnalysisScreen(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp), rememberNavController(), viewModel()
    )
}