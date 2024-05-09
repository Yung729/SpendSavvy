package com.example.spendsavvy.screen.Staff

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spendsavvy.components.SwipeToDeleteItem
import com.example.spendsavvy.components.bounceClick
import com.example.spendsavvy.models.Staff
import com.example.spendsavvy.navigation.Screen
import com.example.spendsavvy.ui.theme.HeaderTitle
import com.example.spendsavvy.ui.theme.poppinsFontFamily
import com.example.spendsavvy.viewModels.StaffViewModel

@Composable
fun StaffScreen(modifier: Modifier, staffViewModel: StaffViewModel, navController: NavController) {

    val staffList by staffViewModel.staffList.observeAsState(initial = emptyList())
    val staffCount by staffViewModel.totalStaffCount.observeAsState(initial = 0)
    val staffSalary by staffViewModel.totalStaffSalary.observeAsState(initial = 0.0)



    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {


        item{
            StaffCard(staffCount = staffCount, staffSalary = staffSalary)
        }

        item {

            Text(
                text = "Staff Record",
                fontFamily = poppinsFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = HeaderTitle
            )


            Spacer(modifier = Modifier.height(10.dp))

            StaffList(
                modifier = Modifier.height(400.dp),
                staffList = staffList,
                navController = navController,
                staffViewModel = staffViewModel
            )
        }

        item{
            Row {
                Button(
                    onClick = {
                        navController.navigate(Screen.AddStaffScreen.route)
                    },
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .bounceClick(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Add Staff",
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }

                Button(
                    onClick = {
                        staffViewModel.addTotalStaffSalaryToExpenses()
                    },
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .bounceClick(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Add Expenses",
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
            }

        }


    }
}


@Composable
fun StaffCard(
    staffCount: Int,
    staffSalary: Double
) {


    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF696161), // Start color
                            Color(0xFF1B1B1B)  // End color
                        )
                    )
                )
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Staff",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Total Staff : $staffCount",
                    modifier = Modifier,
                    color = Color.White

                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Total Salary : RM $staffSalary",
                    fontSize = 16.sp,
                    color = Color.White
                )


            }
        }
    }


}


@Composable
fun StaffList(
    staffList: List<Staff>,
    modifier: Modifier = Modifier,
    navController: NavController,
    staffViewModel: StaffViewModel
) {

    LazyColumn(modifier = modifier) {
        items(staffList) { item: Staff ->


            StaffCard(
                staff = item,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                navController = navController,
                staffViewModel = staffViewModel
            )

        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StaffCard(
    staff: Staff,
    modifier: Modifier = Modifier,
    navController: NavController,
    staffViewModel: StaffViewModel
) {


    val dismissState = rememberDismissState()
    if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
        staffViewModel.deleteStaff(staff)
    }

    if (!dismissState.isDismissed(DismissDirection.StartToEnd)) {


        SwipeToDeleteItem(state = dismissState) {
            Card(
                modifier = modifier.clickable {
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        key = "currentStaff", value = staff
                    )

                    navController.navigate(Screen.StaffDetailScreen.route)

                }, colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {

                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically

                ) {


                    Column(
                        horizontalAlignment = Alignment.Start, modifier = Modifier
                    ) {
                        Text(
                            text = staff.name,
                            fontWeight = FontWeight.SemiBold
                        )

                    }


                    Text(
                        text = staff.salary.toString(),
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF119316),
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            }
        }
    }
}