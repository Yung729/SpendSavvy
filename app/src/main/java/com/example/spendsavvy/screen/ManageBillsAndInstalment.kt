package com.example.spendsavvy.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@Composable
fun ManageBillsAndInstalment(modifier: Modifier = Modifier, navController: NavController) {

    val categories = listOf("Upcoming", "Paid", "Overdue")
    val numbers = listOf("10", "20", "5")
    val categoryButtons = listOf("All", "Upcoming", "Paid", "Overdue")
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(Color.LightGray, Color.DarkGray)
    )

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
                .background(
                    brush = gradientBrush,
                    shape = RoundedCornerShape(20.dp),
                )
        ) {
            Row(
                modifier = Modifier
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
                            color = Color.White,
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

//        BillAndInstalmentList(BillsAndInstalmentsData(), loadSelectedBills())
    }
}

//@Composable
//fun BillAndInstalmentList(billsAndInstalments: List<BillAndInstalment>) {
//    LazyColumn(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        items(billsAndInstalments) { billOrInstalment ->
//            BillOrInstalmentItem(billOrInstalment)
//        }
//    }
//}
//
//@Composable
//fun BillOrInstalmentItem(billOrInstalment: BillAndInstalment) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        // Display bill or instalment details here
//        Text(
//            text = billOrInstalment.title,
//            fontWeight = FontWeight.Bold,
//            fontSize = 16.sp,
//            modifier = Modifier.weight(1f)
//        )
//        Text(
//            text = billOrInstalment.amount,
//            fontSize = 16.sp,
//            modifier = Modifier.padding(end = 16.dp)
//        )
//    }
//}
@Composable
fun CategoryItem(category: String) {
    Text(
        text = category,
        fontWeight = FontWeight.SemiBold,
        color = Color.White,
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
        navController = rememberNavController()
    )
}