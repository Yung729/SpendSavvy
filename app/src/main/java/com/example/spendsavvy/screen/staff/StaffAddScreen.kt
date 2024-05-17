package com.example.spendsavvy.screen.staff

import android.content.Context
import android.health.connect.datatypes.units.Length
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendsavvy.R
import com.example.spendsavvy.components.bounceClick
import com.example.spendsavvy.models.Staff
import com.example.spendsavvy.ui.theme.poppinsFontFamily
import com.example.spendsavvy.viewModels.StaffViewModel

@Composable
fun StaffAddScreen(modifier: Modifier = Modifier, staffViewModel: StaffViewModel) {

    val context = LocalContext.current
    var icNumber by remember {
        mutableStateOf("")
    }

    var staffName by remember {
        mutableStateOf("")
    }

    var salary by remember {
        mutableStateOf("")
    }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {


        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()

        ) {
            Column(
                modifier = Modifier.padding(10.dp)

            ) {

                TextField(
                    value = icNumber,
                    onValueChange = {
                        icNumber = it
                    },
                    placeholder = {
                        Text(
                            text = "002234-32-3332",
                            fontFamily = poppinsFontFamily,
                            fontSize = 15.sp,
                            color = Color.Gray
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    maxLines = 1,
                    label = {
                        Text(
                            text = stringResource(id = R.string.staffIc),
                            fontFamily = poppinsFontFamily,
                            fontSize = 15.sp
                        )
                    }, isError = !isValidICNumber(icNumber), // Set error state based on validation
                    singleLine = true // Ensure single line input
                )
            }
        }


        Spacer(modifier = Modifier.height(30.dp))

        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()

        ) {

            Column(
                modifier = Modifier.padding(10.dp)

            ) {

                TextField(value = staffName, onValueChange = {
                    staffName = it
                }, placeholder = {
                    Text(
                        text = stringResource(id = com.example.spendsavvy.R.string.staff) + " 1",
                        fontFamily = poppinsFontFamily,
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                }, label = {
                    Text(
                        text = stringResource(id = com.example.spendsavvy.R.string.staffName),
                        fontFamily = poppinsFontFamily,
                        fontSize = 15.sp
                    )
                })
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()

        ) {

            Column(
                modifier = Modifier.padding(10.dp)

            ) {

                TextField(value = salary, onValueChange = {
                    salary = it
                }, placeholder = {
                    Text(
                        text = "RM 0",
                        fontFamily = poppinsFontFamily,
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                }, label = {
                    Text(
                        text = stringResource(id = com.example.spendsavvy.R.string.staffSalary),
                        fontFamily = poppinsFontFamily,
                        fontSize = 15.sp
                    )
                }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {

                if(isValidICNumber(icNumber) && staffName.isNotEmpty() && salary.isNotEmpty()){
                    staffViewModel.addStaffToDatabase(
                        Staff(id = icNumber, name = staffName, salary = salary.toDoubleOrNull() ?: 0.0)
                    )
                }else{
                    Toast.makeText(context,"Please fill in all the details",Toast.LENGTH_SHORT).show()
                }


            },
            modifier = Modifier
                .padding(bottom = 10.dp)
                .bounceClick(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            )
        ) {
            Text(
                text = stringResource(id = com.example.spendsavvy.R.string.add),
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }


    }

}

fun isValidICNumber(icNumber: String): Boolean {
    val regex = """^\d{6}-\d{2}-\d{4}$""".toRegex()
    return regex.matches(icNumber)
}
