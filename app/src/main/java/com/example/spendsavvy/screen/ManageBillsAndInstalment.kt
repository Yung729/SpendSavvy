package com.example.spendsavvy.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.spendsavvy.R
import com.example.spendsavvy.models.Bills
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.navigation.Screen
import com.example.spendsavvy.viewModels.BillsViewModel
import com.example.spendsavvy.viewModels.OverviewViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageBillsAndInstalment(modifier: Modifier = Modifier, navController: NavController, billsViewModel: BillsViewModel, transactionViewModel: OverviewViewModel) {

    val categories = listOf("Upcoming", "Paid", "Overdue")
    val options = mutableStateListOf("All", "Upcoming", "Paid", "Overdue")
    var selectedIndex by remember { mutableIntStateOf(0)}
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(Color.LightGray, Color.DarkGray)
    )

    val allBillsList by billsViewModel.allBillsList.observeAsState(initial = emptyList())
    val upcomingBillsList by billsViewModel.upcomingBillsList.observeAsState(initial = emptyList())
    val overdueBillsList by billsViewModel.overdueBillsList.observeAsState(initial = emptyList())
    val paidBillsList by billsViewModel.paidBillsList.observeAsState(initial = emptyList())
    val totalUpcomingBills by billsViewModel.totalUpcomingBills.observeAsState(initial = 0)
    val totalOverdueBills by billsViewModel.totalOverdueBills.observeAsState(initial = 0)
    val totalPaidBills by billsViewModel.totalPaidBills.observeAsState(initial = 0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp, horizontal = 3.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(60.dp)
        ) {
            Row(
                modifier = Modifier
                    .background(
                        brush = gradientBrush,
                        shape = RoundedCornerShape(20.dp),
                    )
                    .border(
                        width = 2.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(20.dp),
                    )
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                categories.forEachIndexed { index, category ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 5.dp)

                    ) {
                        CategoryItem(category)
                        val textColor = when (category) {
                            "Upcoming" -> Color.Yellow
                            "Overdue" -> Color.Red
                            "Paid" -> Color.Green
                            else -> Color.Black
                        }
                        Text(
                            text = when (category) {
                                "Upcoming" -> totalUpcomingBills.toString()
                                "Overdue" -> totalOverdueBills.toString()
                                "Paid" -> totalPaidBills.toString()
                                else -> ""
                            },
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    }
                    if (index < categories.size - 1) {
                        VerticalDivider(
                            color = Color.Black,
                            thickness = 2.dp,
                            modifier = Modifier.height(60.dp)
                        )
                    }
                }
            }
        }
        // Segmented Button Row
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()
        ) {
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
            ) {
                options.forEachIndexed { index, option ->
                    SegmentedButton(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index, count = options.size)
                    ) {
                        Text(
                            text = option,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                        )
                    }
                }
            }
        }

        when (selectedIndex) {
            0 -> BillList(allBillsList, navController, transactionViewModel )
            1 -> BillList(upcomingBillsList, navController, transactionViewModel)
            2 -> BillList(paidBillsList, navController, transactionViewModel)
            3 -> BillList(overdueBillsList, navController, transactionViewModel)
        }
    }

    // FloatingActionButton
    FloatingActionButton(
        onClick = {
            navController.navigate(Screen.AddBills.route)
        },
        modifier = Modifier
            .size(55.dp, 55.dp)
            .offset(x = (325).dp, y = (640).dp),
        shape = RoundedCornerShape(16.dp),
        elevation = FloatingActionButtonDefaults.elevation(8.dp),
        containerColor = Color.Gray,
        contentColor = Color.White,
    ) {
        Image(
            painter = painterResource(id = R.drawable.add_icon),
            contentDescription = "Add bills",
            modifier = Modifier
                .size(50.dp)
        )
    }
}

@Composable
fun BillList(
    bills: List<Bills>,
    navController: NavController,
    transactionViewModel: OverviewViewModel,
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(bills) { bill ->
            BillItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "currentBill", value = bill
                        )
                        navController.navigate(Screen.EditBills.route)
                    },
                bill = bill,
                navController = navController,
                transactionViewModel = transactionViewModel
            )
        }
    }
}

@Composable
fun BillItem(modifier: Modifier = Modifier, bill: Bills, navController: NavController, transactionViewModel: OverviewViewModel) {
    // Display bill details
    Card(
        modifier = modifier
            .padding(vertical = 5.dp, horizontal = 10.dp)
            .border(
                width = 2.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(20.dp),
            )
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Image
            Image(
                painter = rememberAsyncImagePainter(model = bill.category.imageUri),
                contentDescription = "",
                modifier = Modifier
                    .size(60.dp)
                    .padding(8.dp)
            )

            // Description and date
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = bill.description,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = " Due Date: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(bill.selectedDueDate)}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column() {
                // Bills Status
                val textColor = when (bill.billsStatus) {
                    "UPCOMING" -> Color.Yellow
                    "OVERDUE" -> Color.Red
                    "PAID" -> Color.Green
                    else -> Color.Black
                }
                Text(
                    text = bill.billsStatus,
                    style = TextStyle(
                        color = textColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        shadow = Shadow(
                            color = Color.Black,
                            blurRadius = 10f
                        ),
                    ),
                    modifier = Modifier
                        .padding(end = 18.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Amount
                Text(
                    text = String.format("RM%.2f", bill.amount),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .padding(end = 18.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }

        // Actions
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Spacer(modifier = Modifier.width(30.dp))
            Button(
                onClick = { /* Handle mark as paid */ },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                colors = ButtonDefaults.buttonColors(Color.Green),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.tick_icon),
                        contentDescription = "Add to expense",
                        modifier = Modifier
                            .size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Mark as Paid",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.width(40.dp))
            Button(
                onClick = { /* Handle delete */ },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                colors = ButtonDefaults.buttonColors(Color.Red),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.delete_icon),
                        contentDescription = "Delete Bills",
                        modifier = Modifier
                            .size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Delete",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.width(40.dp))
        }
    }
}


@Composable
fun CategoryItem(category: String) {
    Text(
        text = category,
        fontWeight = FontWeight.SemiBold,
        color = Color.Black,
        fontSize = 20.sp,
        modifier = Modifier.padding(horizontal = 12.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun ManageBillsAndInstalmentPreview() {
    ManageBillsAndInstalment(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        navController = rememberNavController(),
        billsViewModel = viewModel(),
        transactionViewModel = viewModel(),
    )
}