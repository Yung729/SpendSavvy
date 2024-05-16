package com.example.spendsavvy.screen.overview

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.spendsavvy.components.ButtonComponent
import com.example.spendsavvy.components.bounceClick
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.navigation.Screen
import com.example.spendsavvy.viewModels.OverviewViewModel
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun TransactionDetail(
    modifier: Modifier,
    transactions: Transactions,
    transactionViewModel: OverviewViewModel
) {


    var updatedTransactionAmount by remember {
        mutableStateOf(transactions.amount.toString())
    }

    var updatedTransactionDescription by remember {
        mutableStateOf(transactions.description)
    }

    val calendarState = rememberSheetState()
    val updatedDate = remember { mutableStateOf<Date>(transactions.date) }
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())


    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = rememberAsyncImagePainter(model = transactions.category.imageUri),
            contentDescription = "",
            modifier = Modifier
                .size(30.dp, 30.dp)
                .padding(end = 10.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(onClick = { calendarState.show() },modifier = Modifier.fillMaxWidth()) {
            Text(text = updatedDate.value.let { dateFormat.format(it) } ?: "Select Date")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Transaction Name : ${transactions.category.categoryName}")


        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = updatedTransactionAmount,
            onValueChange = { updatedTransactionAmount = it },
            label = { Text(text = "Amount") },
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp),
            shape = RoundedCornerShape(15.dp),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(8.dp))

        CalendarDialog(
            state = calendarState,
            config = CalendarConfig(
                monthSelection = true,
                yearSelection = true,
                style = CalendarStyle.MONTH
            ),
            selection = CalendarSelection.Date { date ->
                val selectedDateValue =
                    Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant())
                updatedDate.value = selectedDateValue
            })


        OutlinedTextField(
            value = updatedTransactionDescription,
            onValueChange = { updatedTransactionDescription = it },
            label = { Text(text = "Description") },
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp),
            shape = RoundedCornerShape(15.dp),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(8.dp))

        ButtonComponent(
            onButtonClick = {
                transactionViewModel.editTransaction(
                    transactions = transactions,
                    updatedTransactions = Transactions(
                        id = transactions.id,
                        amount = updatedTransactionAmount.toDoubleOrNull() ?: 0.0,
                        category = Category(
                            id = transactions.category.id,
                            imageUri = transactions.category.imageUri,
                            categoryName = transactions.category.categoryName,
                            categoryType = transactions.category.categoryType
                        ),
                        paymentMethod = transactions.paymentMethod,
                        description = updatedTransactionDescription,
                        transactionType = transactions.transactionType,
                        date = updatedDate.value

                    )
                )

            },
            text = "Edit"
        )

    }


}