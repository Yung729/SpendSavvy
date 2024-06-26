package com.example.spendsavvy.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spendsavvy.R
import com.example.spendsavvy.viewModels.BillsViewModel
import com.example.spendsavvy.viewModels.NotificationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Notification(modifier: Modifier = Modifier, navController: NavController, billsViewModel: BillsViewModel, notificationViewModel: NotificationViewModel) {

    val scaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    var isToggled by remember { mutableStateOf(notificationViewModel.getNotificationStatus()) }

    // Get the current notification status from the ViewModel
    var isNotificationEnabled by remember { mutableStateOf(notificationViewModel.getNotificationStatus()) }

    // Toggle notification status
    val toggleNotification: () -> Unit = {
        isNotificationEnabled = !isNotificationEnabled
        // Update the notification status in the ViewModel
        notificationViewModel.isNotificationEnabled(isNotificationEnabled)
        // Pass the notification status to BillsViewModel
        billsViewModel.updateNotificationStatus(isNotificationEnabled)
    }

    androidx.compose.material.Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        content = {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.text_16),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.bell_icon),
                        contentDescription = "",
                        modifier = Modifier.size(25.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = stringResource(id = com.example.spendsavvy.R.string.notifications),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(10.dp)
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Image(
                            painter = painterResource(if (isToggled) R.drawable.toggle_on_icon else R.drawable.toggle_off_icon),
                            contentDescription = if (isToggled) "TOGGLE ON" else "TOGGLE OFF",
                            modifier = Modifier
                                .size(40.dp)
                                .clickable {
                                    isToggled = !isToggled

                                    // Update the notification status in the view model
                                    notificationViewModel.isNotificationEnabled(isToggled)

                                    val message =
                                        if (isToggled) "Notification is on" else "Notification is off"
                                    coroutineScope.launch {
                                        scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                                        scaffoldState.snackbarHostState.showSnackbar(message = message)
                                    }
                                }
                        )
                    }
                }
                LineDivider()
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun NotificationPreview() {
    Notification(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        navController = rememberNavController(),
        billsViewModel = viewModel(),
        notificationViewModel = viewModel()
    )
}