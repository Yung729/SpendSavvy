package com.example.spendsavvy.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
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
import com.example.spendsavvy.components.bounceClick
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.viewModels.OverviewViewModel

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

        OutlinedTextField(
            value = updatedTransactionDescription,
            onValueChange = { updatedTransactionDescription = it },
            label = { Text(text = "Amount") },
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp),
            shape = RoundedCornerShape(15.dp),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(8.dp))


        Button(
            onClick = {
                transactionViewModel.editTransaction(
                    transactions = transactions,
                    updatedTransactions = Transactions(
                        amount = updatedTransactionAmount.toDoubleOrNull() ?: 0.0,
                        category = Category(
                            imageUri = transactions.category.imageUri,
                            categoryName = transactions.category.categoryName,
                            categoryType = transactions.category.categoryType
                        ),
                        description = updatedTransactionDescription,
                        transactionType = transactions.transactionType,
                        date = transactions.date

                    )
                )

            },
            modifier = Modifier
                .padding(bottom = 10.dp)
                .bounceClick(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            )
        ) {
            Text(
                text = "Update", textAlign = TextAlign.Center, color = Color.White
            )
        }


    }


}