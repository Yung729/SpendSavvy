package com.example.spendsavvy.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.spendsavvy.R
import com.example.spendsavvy.components.bounceClick
import com.example.spendsavvy.models.Bills
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.navigation.Screen
import com.example.spendsavvy.ui.theme.HeaderTitle
import com.example.spendsavvy.ui.theme.poppinsFontFamily
import com.example.spendsavvy.viewModels.CategoryViewModel
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


@Composable
fun ManageBillsAndInstalment(modifier: Modifier = Modifier, navController: NavController, transactionViewModel: OverviewViewModel) {

    val categories = listOf("Upcoming", "Paid", "Overdue")
    val numbers = listOf("10", "20", "5")
    val categoryButtons = listOf("All", "Upcoming", "Paid", "Overdue")
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(Color.LightGray, Color.DarkGray)
    )

    val billsList: List<Bills> = emptyList()

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
                        Text(
                            text = numbers.getOrNull(index) ?: "",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CategoryButtons(categoryButtons = categoryButtons)
        }

//        // Bill list
//        LazyColumn(
//            modifier = modifier,
//            verticalArrangement = Arrangement.spacedBy(20.dp)
//        ) {
//            item {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    // Provide options to sort bills
//                    BillList(
//                        modifier = Modifier.height(400.dp),
//                        bills = billsList, // Pass the list of bills
//                        navController = navController,
//                        transactionViewModel = transactionViewModel
//                    )
//                }
//            }
//        }
        // FloatingActionButton
        FloatingActionButton(
            onClick = {
                navController.navigate(Screen.AddBills.route)
            },
            modifier = Modifier
                .size(55.dp, 55.dp)
                .offset(
                    x = (300).dp, y = (475).dp
                ),
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
}

//@Composable
//fun BillList(
//    modifier: Modifier = Modifier,
//    bills: List<Bills>,
//    navController: NavController,
//    transactionViewModel: OverviewViewModel
//) {
//    LazyColumn(
//        modifier = modifier.fillMaxSize()
//    ) {
//        items(bills.size) { index ->
//            BillItem(
//                modifier = modifier,
//                bill = bills[index],
//                navController = navController,
//                transactionViewModel = transactionViewModel
//            )
//        }
//    }
//}
//
//@Composable
//fun BillItem(modifier: Modifier = Modifier, bill: Bills, navController: NavController, transactionViewModel: OverviewViewModel) {
//    // Display bill details
//    Card(
//        modifier = modifier
//            .padding(10.dp)
//            .border(
//                width = 2.dp,
//                color = Color.Gray,
//                shape = RoundedCornerShape(20.dp),
//            )
//            .fillMaxWidth(),
//        elevation = CardDefaults.cardElevation(
//            defaultElevation = 8.dp
//        )
//    ) {
//        Row(
//            modifier = Modifier
//                .padding(8.dp)
//                .fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            // Category Image
//            Image(
//                painter = rememberAsyncImagePainter(model = bill.category.imageUri),
//                contentDescription = "",
//                modifier = Modifier
//                    .size(60.dp)
//                    .padding(8.dp)
//            )
//
//            // Description and date
//            Column(
//                modifier = Modifier.weight(1f)
//            ) {
//                Spacer(modifier = Modifier.height(8.dp))
//                Text(
//                    text = bill.description,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 16.sp
//                )
//                Spacer(modifier = Modifier.height(4.dp))
//                Text(
//                    text = "Due Date: ${bill.selectedDueDate}",
//                    fontSize = 14.sp,
//                    color = Color.Gray
//                )
//            }
//            Spacer(modifier = Modifier.width(10.dp))
//            Column() {
//                // Bills Status
//                Text(
//                    text = bill.billsStatus,
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.Red,
//                    textAlign = TextAlign.End,
//                    modifier = Modifier
//                        .padding(end = 18.dp)
//                        .align(Alignment.CenterHorizontally)
//                )
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Amount
//                Text(
//                    text = bill.amount.toString(),
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.Black,
//                    textAlign = TextAlign.End,
//                    modifier = Modifier
//                        .padding(end = 18.dp)
//                        .align(Alignment.CenterHorizontally)
//                )
//            }
//        }
//
//        // Actions
//        Row(
//            modifier = Modifier
//                .padding(horizontal = 8.dp, vertical = 4.dp)
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            Spacer(modifier = Modifier.width(30.dp))
//            Button(
//                onClick = { /* Handle mark as paid */ },
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(horizontal = 4.dp, vertical = 2.dp),
//                colors = ButtonDefaults.buttonColors(Color.Green),
//                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
//            ) {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Image(
//                        painter = painterResource(id = R.drawable.tick_icon),
//                        contentDescription = "Add to expense",
//                        modifier = Modifier
//                            .size(16.dp) // Decrease image size
//                    )
//                    Spacer(modifier = Modifier.width(4.dp))
//                    Text(
//                        text = "Mark as Paid",
//                        fontWeight = FontWeight.SemiBold,
//                        fontSize = 12.sp,
//                        color = Color.Black
//                    )
//                }
//            }
//            Spacer(modifier = Modifier.width(40.dp))
//            Button(
//                onClick = { /* Handle delete */ },
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(horizontal = 4.dp, vertical = 2.dp),
//                colors = ButtonDefaults.buttonColors(Color.Red),
//                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
//            ) {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Image(
//                        painter = painterResource(id = R.drawable.delete_icon),
//                        contentDescription = "Delete Bills",
//                        modifier = Modifier
//                            .size(16.dp)
//                    )
//                    Spacer(modifier = Modifier.width(4.dp))
//                    Text(
//                        text = "Delete",
//                        fontWeight = FontWeight.SemiBold,
//                        fontSize = 12.sp,
//                        color = Color.Black
//                    )
//                }
//            }
//            Spacer(modifier = Modifier.width(40.dp))
//        }
//    }
//}


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

@Composable
fun CategoryButtons(categoryButtons: List<String>) {
    Row(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 3.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        categoryButtons.forEach { category ->
            Button(
                onClick = { /* Handle button click */ },
                modifier = Modifier.padding(horizontal = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = category,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ManageBillsAndInstalmentPreview() {
    ManageBillsAndInstalment(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        navController = rememberNavController(),
        transactionViewModel = viewModel(),
    )
}