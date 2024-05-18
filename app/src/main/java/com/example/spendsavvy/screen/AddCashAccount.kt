package com.example.spendsavvy.screen

import android.R
import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spendsavvy.models.Cash
import com.example.spendsavvy.ui.theme.poppinsFontFamily
import com.example.spendsavvy.viewModels.WalletViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState", "RememberReturnType")
@Composable
fun AddCashAccountScreen(
    walletViewModel: WalletViewModel,
    navController: NavController
) {
    val context = LocalContext.current

    var typeName by remember {
        mutableStateOf("")
    }

    var initialAmt by remember {
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
            .fillMaxWidth(0.85f)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.padding(start = 30.dp, top = 15.dp, bottom = 30.dp)
        ) {
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

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Enter your bank",
                fontFamily = poppinsFontFamily,
                fontSize = 15.sp
            )

            TextField(
                value = typeName,
                onValueChange = {
                    typeName = it
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
                placeholder = {
                    Text(
                        text = "Public Bank",
                        fontFamily = poppinsFontFamily,
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                }
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = stringResource(id = com.example.spendsavvy.R.string.initAmount),
                fontFamily = poppinsFontFamily,
                fontSize = 15.sp
            )

            TextField(
                value = initialAmt,
                onValueChange = {
                    initialAmt = it
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
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

        }

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier
                .padding(30.dp),
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
                    if ((initialAmt.toDoubleOrNull() ?: 0.0) <= 0.00) {
                        Toast.makeText(
                            context,
                            "Please enter the amount that is more than RM 0.00",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {
                        walletViewModel.addCashDetailsToDatabase(
                            Cash(
                                imageUri = selectedImageUri,
                                typeName = typeName,
                                balance = initialAmt.toDoubleOrNull() ?: 0.0
                            ), selectedImageUri
                        )
                    }
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