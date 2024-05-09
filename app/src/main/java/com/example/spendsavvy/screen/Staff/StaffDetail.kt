package com.example.spendsavvy.screen.Staff

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.spendsavvy.components.bounceClick
import com.example.spendsavvy.models.Staff
import com.example.spendsavvy.viewModels.StaffViewModel


@Composable
fun StaffDetailScreen(
    modifier: Modifier,
    staff: Staff,
    staffViewModel: StaffViewModel
) {

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
            label = { Text(text = "Staff IC No.") },
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp),
            shape = RoundedCornerShape(15.dp),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = updatedStaffName,
            onValueChange = { updatedStaffName = it },
            label = { Text(text = "Staff Name.") },
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
            onValueChange = { updatedStaffSalary = it },
            label = { Text(text = "Staff IC No.") },
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp),
            shape = RoundedCornerShape(15.dp),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                staffViewModel.editStaff(
                    staff = staff,
                    updatedStaff = Staff(
                        id = updatedStaffIC,
                        name = updatedStaffName,
                        salary = updatedStaffSalary.toDoubleOrNull() ?: 0.0
                    )
                )

            },
            modifier = Modifier
                .padding(bottom = 10.dp)
                .bounceClick(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            )
        ) {
            Text(
                text = "Update", textAlign = TextAlign.Center, color = Color.White
            )
        }


    }


}