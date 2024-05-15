package com.example.spendsavvy.screen

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spendsavvy.models.FDAccount
import com.example.spendsavvy.navigation.Screen
import com.example.spendsavvy.ui.theme.poppinsFontFamily
import com.example.spendsavvy.viewModels.WalletViewModel
import java.text.DateFormat
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import kotlin.time.toDuration
import android.R
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState", "RememberReturnType")
@Composable
fun FixedDepositDetailsScreen(
    modifier: Modifier,
    walletViewModel: WalletViewModel,
    navController: NavController
) {
    val currentDate = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

    var selectedBank by remember {
        mutableStateOf("")
    }

    var depositAmt by remember {
        mutableStateOf("")
    }

    var interestRate by remember {
        mutableStateOf("")
    }

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            selectedImageUri = it
        })

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 15.dp, top = 15.dp
            )
    ) {
        Text(
            text = stringResource(id = com.example.spendsavvy.R.string.fixedDep),
            fontFamily = poppinsFontFamily,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(15.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 15.dp)
        ) {
            Text(
                text = stringResource(id = com.example.spendsavvy.R.string.text_10),
                fontFamily = poppinsFontFamily,
                fontSize = 15.sp,
                color = Color.Red
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(id = com.example.spendsavvy.R.string.pickIconPhoto),
                fontFamily = poppinsFontFamily,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF46484B), contentColor = Color.White
                ),
                onClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) {

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_input_add),
                        contentDescription = "Add Image"
                    )

                    Text(
                        text = stringResource(id = com.example.spendsavvy.R.string.pickAPhoto),
                        fontFamily = poppinsFontFamily,
                        fontSize = 15.sp
                    )
                }
            }

            if (selectedImageUri != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Image added", color = Color.Green
                    )
                    IconButton(
                        onClick = { selectedImageUri = null },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear Image"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(id = com.example.spendsavvy.R.string.text_11),
                fontFamily = poppinsFontFamily,
                fontSize = 15.sp
            )

            ClickableText(
                text = AnnotatedString(stringResource(id = com.example.spendsavvy.R.string.text_12)),
                onClick = {
                    navController.navigate(Screen.AddCashAccount.route)
                }
            )

            /*ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it }
            ) {
                TextField(
                    value = searchBank,
                    onValueChange = {
                        searchBank = it
                    },
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
                    for (bankAccount in cashDetailsList) {
                        DropdownMenuItem(
                            text = {
                                Text(text = bankAccount.typeName)
                            },
                            onClick = {
                                searchBank = bankAccount.typeName
                                isExpanded = false
                            }
                        )
                    }
                }
            }*/
            TextField(
                value = selectedBank,
                onValueChange = {
                    selectedBank = it
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
                placeholder = {
                    Text(
                        text = "Public bank",
                        fontFamily = poppinsFontFamily,
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                }
            )

            Spacer(modifier = Modifier.height(15.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 15.dp)
        ) {
            Text(
                text = stringResource(id = com.example.spendsavvy.R.string.depositAmount),
                fontFamily = poppinsFontFamily,
                fontSize = 15.sp
            )

            TextField(
                value = depositAmt,
                onValueChange = {
                    depositAmt = it
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Decimal
                ),
                placeholder = {
                    Text(
                        text = "RM 0.00",
                        fontFamily = poppinsFontFamily,
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                }
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = stringResource(id = com.example.spendsavvy.R.string.interestRate),
                fontFamily = poppinsFontFamily,
                fontSize = 15.sp
            )


            TextField(
                value = interestRate,
                onValueChange = {
                    interestRate = it
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Decimal
                ),
                placeholder = {
                    Text(
                        text = "0.00",
                        fontFamily = poppinsFontFamily,
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                }
            )
        }


        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier
                .padding(15.dp),
            horizontalArrangement = Arrangement.spacedBy(30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    navController.navigateUp()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(id = com.example.spendsavvy.R.string.cancel),
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFontFamily
                )
            }

            Button(
                onClick = {
                    walletViewModel.addFDDetailsToDatabase(
                        FDAccount(
                            selectedImageUri,
                            selectedBank,
                            interestRate.toDoubleOrNull() ?: 0.0,
                            depositAmt.toDoubleOrNull() ?: 0.0,
                            currentDate
                        ), selectedImageUri
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(id = com.example.spendsavvy.R.string.add),
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFontFamily
                )
            }
        }
    }
}
