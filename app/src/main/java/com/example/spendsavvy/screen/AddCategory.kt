package com.example.spendsavvy.screen

import android.R
import android.annotation.SuppressLint
import android.net.Uri
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.spendsavvy.components.bounceClick
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.viewModels.CategoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun AddCategoryScreen(
    modifier: Modifier,
    catViewModel: CategoryViewModel
) {
    val options = mutableStateListOf("Expenses", "Income")
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    var categoryType by remember { mutableStateOf("") }
    var categoryName by remember { mutableStateOf("") }
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var error by remember { mutableStateOf(false) }


    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            selectedImageUri = it
        })


    Column(
        modifier = modifier
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Pick Icon Photo",
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(modifier = Modifier
                .fillMaxWidth()
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
                        text = "Pick a Photo",
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

            Spacer(modifier = Modifier.height(8.dp))


            OutlinedTextField(
                value = categoryName,
                onValueChange = { categoryName = it },
                label = { Text(text = "Category Name") },
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp, top = 10.dp),
                shape = RoundedCornerShape(15.dp),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()
            ) {
                SingleChoiceSegmentedButtonRow {
                    options.forEachIndexed { index, option ->
                        SegmentedButton(
                            selected = selectedIndex == index,
                            onClick = { selectedIndex = index },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index, count = options.size
                            )
                        ) {
                            Text(text = option)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            categoryType = if (selectedIndex == 0) {
                "Expenses"
            } else {
                "Incomes"
            }


            Button(
                onClick = {
                    if (selectedImageUri != null && categoryName.isNotEmpty()) {
                        // If all fields have data, add the category
                        catViewModel.addCategoryToDatabase(
                            Category(
                                imageUri = selectedImageUri,
                                categoryName = categoryName,
                                categoryType = categoryType
                            ), selectedImageUri
                        )
                    } else {
                        // If any field is empty, show error
                        error = true
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
                    text = "Add", textAlign = TextAlign.Center, color = Color.White
                )
            }

            if (error) {
                Text(
                    text = "All fields are required",
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }


        }

    }
}