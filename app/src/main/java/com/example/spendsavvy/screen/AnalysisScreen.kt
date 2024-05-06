package com.example.spendsavvy.screen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.components.RemainingChart
import com.example.spendsavvy.components.bounceClick
import com.example.spendsavvy.navigation.Screen
import com.example.spendsavvy.viewModels.OverviewViewModel
import com.example.spendsavvy.viewModels.TargetViewModel
import java.time.YearMonth
import kotlin.math.roundToInt


@Composable
fun AnalysisScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    transactionViewModel: OverviewViewModel,
    budgetViewModel: TargetViewModel
) {
    val scrollState = rememberScrollState()
    val expensesData by transactionViewModel.currentMonthExpenses.observeAsState(initial = 0.0)
    val incomesData by transactionViewModel.currentMonthIncomes.observeAsState(initial = 0.0)

    val budgetAmountFromDB = budgetViewModel.budget.observeAsState(initial = 0.0)
    val goalAmountFromDB = budgetViewModel.goal.observeAsState(initial = 0.0)

    // Get the current year and month
    val currentYearMonth = YearMonth.now()

    // Get the total days in the current month
    val totalDaysInMonth = currentYearMonth.lengthOfMonth()

    // Calculate the monthly budget amount multiplied by the total days in the current month
    val monthlyBudgetAmount = budgetAmountFromDB.value * totalDaysInMonth
    val monthlyGoalAmount = goalAmountFromDB.value * totalDaysInMonth

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
                text = "Set Budget & Goal",
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Transaction Analysis",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(30.dp))

        PieChart(
            data = mapOf(
                Pair("Expenses", expensesData.roundToInt()),
                Pair("Incomes", incomesData.roundToInt())
            )
        )

        LineDivider()
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Budget Analysis",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth()
        )


        Text(
            text = "Monthly Budget : $monthlyBudgetAmount",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            modifier = Modifier.fillMaxWidth()
        )


        RemainingChart(
            indicatorValue = expensesData,
            maxIndicatorValue = monthlyBudgetAmount,
        )

        LineDivider()
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Goal Analysis",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth()
        )


        Text(
            text = "Monthly Goal : $monthlyGoalAmount",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            modifier = Modifier.fillMaxWidth()
        )


        RemainingChart(
            indicatorValue = incomesData - expensesData,
            maxIndicatorValue = monthlyGoalAmount,
        )


    }
}


@Preview(showBackground = true)
@Composable
fun AnalysisScreenPreview() {
    AnalysisScreen(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp), rememberNavController(), viewModel(), viewModel()
    )
}