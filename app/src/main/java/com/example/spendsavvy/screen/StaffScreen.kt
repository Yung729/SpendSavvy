package com.example.spendsavvy.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.DismissState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.spendsavvy.R
import com.example.spendsavvy.components.ButtonComponent
import com.example.spendsavvy.components.SwipeToDeleteItem
import com.example.spendsavvy.models.Staff
import com.example.spendsavvy.navigation.Screen
import com.example.spendsavvy.ui.theme.HeaderTitle
import com.example.spendsavvy.ui.theme.ScreenSize
import com.example.spendsavvy.ui.theme.WindowType
import com.example.spendsavvy.ui.theme.poppinsFontFamily
import com.example.spendsavvy.viewModels.StaffViewModel
import com.example.spendsavvy.viewModels.WalletViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StaffScreen(modifier: Modifier, staffViewModel: StaffViewModel, navController: NavController,walletViewModel : WalletViewModel,window:ScreenSize) {

    val staffList by staffViewModel.staffList.observeAsState(initial = emptyList())
    val staffCount by staffViewModel.totalStaffCount.observeAsState(initial = 0)
    val staffSalary by staffViewModel.totalStaffSalary.observeAsState(initial = 0.0)

    val dismissStateMap = remember { mutableMapOf<Staff, DismissState>() }

    var isPopUp by remember {
        mutableStateOf(false)
    }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {


        item {
            StaffCard(staffCount = staffCount, staffSalary = staffSalary)
        }

        item {

            Text(
                text = stringResource(id = R.string.staffRecord),
                fontFamily = poppinsFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = HeaderTitle
            )


            Spacer(modifier = Modifier.height(10.dp))

            when (window.height) {
                WindowType.Expanded -> {

                    StaffList(
                        modifier = Modifier.height(770.dp),
                        staffList = staffList,
                        navController = navController,
                        staffViewModel = staffViewModel,
                        dismissStateMap = dismissStateMap
                    )
                }

                else ->{

                    StaffList(
                        modifier = Modifier.height(280.dp),
                        staffList = staffList,
                        navController = navController,
                        staffViewModel = staffViewModel,
                        dismissStateMap = dismissStateMap
                    )
                }

            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ButtonComponent(
                    onButtonClick = { navController.navigate(Screen.AddStaffScreen.route) },
                    text = stringResource(id = com.example.spendsavvy.R.string.addStaff)
                )

                ButtonComponent(
                    onButtonClick = {
                        isPopUp = true
                    },
                    text = stringResource(id = com.example.spendsavvy.R.string.addExpense)
                )

            }

        }


    }

    if (isPopUp)
        AddExpensesDialog(
            onCancelClick = { isPopUp = false },
            walletViewModel = walletViewModel,
            staffViewModel = staffViewModel
        )
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
                    text = stringResource(id = com.example.spendsavvy.R.string.staff),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(id = com.example.spendsavvy.R.string.totalStaff) + " : $staffCount",
                    modifier = Modifier,
                    color = Color.White

                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(id = com.example.spendsavvy.R.string.totalSalary) + " : RM $staffSalary",
                    fontSize = 16.sp,
                    color = Color.White
                )


            }
        }
    }


}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StaffList(
    staffList: List<Staff>,
    modifier: Modifier = Modifier,
    navController: NavController,
    staffViewModel: StaffViewModel,
    dismissStateMap: MutableMap<Staff, DismissState>
) {

    LazyColumn(modifier = modifier) {
        items(staffList) { item: Staff ->


            StaffCard(
                staff = item,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                navController = navController,
                staffViewModel = staffViewModel,
                dismissState = dismissStateMap[item]
                    ?: rememberDismissState().also { dismissStateMap[item] = it }

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
    staffViewModel: StaffViewModel,
    dismissState: DismissState
) {


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



        if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
            staffViewModel.deleteStaff(staff)
        }

    }


}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState", "RememberReturnType")
@Composable
fun AddExpensesDialog(
    onCancelClick: () -> Unit,
    walletViewModel: WalletViewModel,
    staffViewModel: StaffViewModel
) {
    val cashDetailsList by walletViewModel.cashDetailsList.observeAsState(initial = emptyList())

    var searchAccount by remember {
        mutableStateOf("")
    }

    var isExpanded by remember {
        mutableStateOf(false)
    }


    Dialog(
        onDismissRequest = { onCancelClick() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .border(1.dp, color = Color.Gray, shape = RoundedCornerShape(15.dp))
                .shadow(elevation = 15.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                Text(
                    "Add Expenses",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )

                Spacer(modifier = Modifier.height(30.dp))


                Text(
                    "Cash Account"
                )

                Spacer(modifier = Modifier.height(25.dp))


                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = { isExpanded = it }
                ) {
                    TextField(
                        value = searchAccount,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenuBox(
                        expanded = isExpanded,
                        onExpandedChange = { isExpanded = it }
                    ) {
                        TextField(
                            value = searchAccount,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                            },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = isExpanded,
                            onDismissRequest = { isExpanded = false }
                        ) {
                            for (cash in cashDetailsList) {          //read from existing stock items
                                DropdownMenuItem(
                                    text = {
                                        Text(text = cash.typeName)
                                    },
                                    onClick = {
                                        searchAccount = cash.typeName
                                        isExpanded = false
                                    }
                                )
                            }
                        }
                    }

                }

                Spacer(modifier = Modifier.height(30.dp))

                Box(
                    Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                    )
                    OutlinedButton(
                        onClick = {
                            staffViewModel.addTotalStaffSalaryToExpenses(searchAccount)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Add Salary",
                            fontWeight = FontWeight.Bold,
                            fontFamily = poppinsFontFamily
                        )
                    }
                }
            }

        }
    }

}