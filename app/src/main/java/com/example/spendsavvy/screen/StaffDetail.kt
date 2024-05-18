package com.example.spendsavvy.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.spendsavvy.R
import com.example.spendsavvy.components.bounceClick
import com.example.spendsavvy.models.Staff
import com.example.spendsavvy.viewModels.StaffViewModel


@Composable
fun StaffDetailScreen(
    modifier: Modifier,
    staff: Staff,
    staffViewModel: StaffViewModel
) {
    val context = LocalContext.current
    var updatedStaffIC by remember {
        mutableStateOf(staff.id)
    }

    var updatedStaffName by remember {
        mutableStateOf(staff.name)
    }

    var updatedStaffSalary by remember {
        mutableStateOf(staff.salary.toString())
    }
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        OutlinedTextField(
            value = updatedStaffIC,
            onValueChange = { updatedStaffIC = it },
            label = { Text(text = stringResource(id = R.string.staffIc) + " No.") },
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp),
            shape = RoundedCornerShape(15.dp),
            singleLine = true, isError = !isValidICNumber(updatedStaffIC),
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = updatedStaffName,
            onValueChange = {
                updatedStaffName = it
            },
            label = { Text(text = stringResource(id = com.example.spendsavvy.R.string.staffName) + ".") },
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp),
            shape = RoundedCornerShape(15.dp),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = updatedStaffSalary,
            onValueChange = {
                updatedStaffSalary = it
            },
            label = { Text(text = stringResource(id = com.example.spendsavvy.R.string.staffSalary)) },
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp),
            shape = RoundedCornerShape(15.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (isValidICNumber(updatedStaffIC) && updatedStaffName.isNotEmpty() && (updatedStaffSalary.toDoubleOrNull()
                        ?: 0.0) > 0.0
                ) {
                    staffViewModel.editStaff(
                        staff = staff,
                        updatedStaff = Staff(
                            id = updatedStaffIC,
                            name = updatedStaffName,
                            salary = updatedStaffSalary.toDoubleOrNull() ?: 0.0
                        )
                    )
                } else {

                    if (!isValidICNumber(updatedStaffIC)) {
                        Toast.makeText(context, "Invalid Ic Number", Toast.LENGTH_SHORT).show()
                    } else if (updatedStaffName.isEmpty()) {
                        Toast.makeText(context, "Please fill in staff name", Toast.LENGTH_SHORT)
                            .show()
                    } else if ((updatedStaffSalary.toDoubleOrNull()
                            ?: 0.0) <= 0.0) {
                        Toast.makeText(context, "Please fill in staff salary", Toast.LENGTH_SHORT)
                            .show()
                    }


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
                text = stringResource(id = com.example.spendsavvy.R.string.update),
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }


    }


}

