package com.example.spendsavvy.screen

import android.R
import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.spendsavvy.components.ButtonComponent
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.viewModels.CategoryViewModel


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun CategoryDetail(
    modifier: Modifier,
    category: Category,
    catViewModel: CategoryViewModel
) {

    val options = mutableStateListOf(stringResource(id = com.example.spendsavvy.R.string.expense),stringResource(id = com.example.spendsavvy.R.string.income))

    var selectedIndex by remember {
        mutableIntStateOf(if (category.categoryType == "Expenses") 0 else 1)
    }


    var updatedCategoryName by remember {
        mutableStateOf(category.categoryName)
    }

    var updatedImageUri by remember {
        mutableStateOf<Uri?>(category.imageUri)
    }

    var updatedCategoryType by remember {
        mutableStateOf(category.categoryType)
    }

    var updatedSelectedIndex by remember {
        mutableIntStateOf(
            when (updatedCategoryType) {
                "Expenses" -> {
                    0
                }
                "Incomes" -> {
                    1
                }
                else -> {
                    2
                }
            }
        )
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            updatedImageUri = it
        })




    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (category.imageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(model = category.imageUri),
                contentDescription = "",
                modifier = Modifier
                    .size(50.dp, 50.dp)
                    .padding(end = 10.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))


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
                    text = stringResource(id = com.example.spendsavvy.R.string.pickAPhoto),
                )
            }
        }


        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = updatedCategoryName,
            onValueChange = { updatedCategoryName = it },
            label = { Text(text = stringResource(id = com.example.spendsavvy.R.string.categoryName)) },
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
                        onClick = {
                            updatedSelectedIndex = index
                            selectedIndex = index
                        },
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

        updatedCategoryType = if (updatedSelectedIndex == 0) {
            "Expenses"
        } else {
            "Incomes"
        }


        ButtonComponent(
            onButtonClick = {
                catViewModel.editCategory(
                    category = category,
                    updatedCategory = Category(
                        id = category.id,
                        imageUri = updatedImageUri,
                        categoryName = updatedCategoryName,
                        categoryType = updatedCategoryType
                    )
                )

            },
            text = stringResource(id = com.example.spendsavvy.R.string.update)
        )




    }


}