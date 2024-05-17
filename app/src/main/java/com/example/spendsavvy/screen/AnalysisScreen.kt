package com.example.spendsavvy.screen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spendsavvy.R
import com.example.spendsavvy.components.PieChart
import com.example.spendsavvy.components.RemainingChart
import com.example.spendsavvy.components.bounceClick
import com.example.spendsavvy.navigation.Screen
import com.example.spendsavvy.screen.LineDivider
import com.example.spendsavvy.ui.theme.ScreenSize
import com.example.spendsavvy.ui.theme.WindowType
import com.example.spendsavvy.viewModels.OverviewViewModel
import com.example.spendsavvy.viewModels.TargetViewModel
import java.time.YearMonth
import kotlin.math.roundToInt


@Composable
fun AnalysisScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    transactionViewModel: OverviewViewModel,
    budgetViewModel: TargetViewModel,
    window : ScreenSize
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
                text = stringResource(id = R.string.setBudgetGoal),
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column {
            Text(
                text = stringResource(id = com.example.spendsavvy.R.string.transactionAnal),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp))

            PieChart(
                data = mapOf(
                    Pair(stringResource(id = R.string.expense), expensesData.roundToInt()),
                    Pair(stringResource(id = R.string.income), incomesData.roundToInt())
                )
            )
        }

        LineDivider()
        Spacer(modifier = Modifier.height(20.dp))

        Column {
            Text(
                text = stringResource(id = com.example.spendsavvy.R.string.budgetAnal),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth()
            )


            Text(
                text = stringResource(id = com.example.spendsavvy.R.string.monthlyBudget) + String.format(": RM %.2f", monthlyBudgetAmount),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )


            when (window.height) {
                WindowType.Expanded -> {
                    RemainingChart(
                        canvasSize = 500.dp,
                        indicatorValue = expensesData,
                        maxIndicatorValue = monthlyBudgetAmount,
                    )
                }

                else -> {
                    RemainingChart(
                        indicatorValue = expensesData,
                        maxIndicatorValue = monthlyBudgetAmount,
                    )
                }
            }

        }

        LineDivider()
        Spacer(modifier = Modifier.height(20.dp))

        Column {
            Text(
                text = stringResource(id = com.example.spendsavvy.R.string.goalAnal),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth()
            )


            Text(
                text = stringResource(id = com.example.spendsavvy.R.string.monthlyGoal) + String.format(": RM %.2f", monthlyGoalAmount),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )




            when (window.height) {
                WindowType.Expanded -> {
                    RemainingChart(
                        canvasSize = 500.dp,
                        indicatorValue = if ((incomesData - expensesData) >= 0){ incomesData - expensesData} else {0.0},
                        maxIndicatorValue = monthlyGoalAmount,
                    )
                }

                else -> {
                    RemainingChart(
                        indicatorValue = if ((incomesData - expensesData) >= 0){ incomesData - expensesData} else {0.0},
                        maxIndicatorValue = monthlyGoalAmount,
                    )
                }
            }


        }


    }
}

