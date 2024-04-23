package com.example.spendsavvy.screen

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.spendsavvy.components.SwipeToDeleteItem
import com.example.spendsavvy.components.bounceClick
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.viewModels.CategoryViewModel

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier, catViewModel: CategoryViewModel = viewModel()
) {


    val options = mutableStateListOf("Expenses", "Income")
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }
    val openAlertDialog = remember { mutableStateOf(false) }

    val expenseList by catViewModel.expensesList.observeAsState(initial = emptyList())
    val incomeList by catViewModel.incomeList.observeAsState(initial = emptyList())




    Column(modifier = modifier) {


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

        Box(modifier = Modifier.fillMaxSize()) {


            if (selectedIndex == 0) {
                CategoryList(categoryList = expenseList)
            } else if (selectedIndex == 1) {
                CategoryList(categoryList = incomeList)
            }





            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End
                ) {
                    Button(
                        onClick = { openAlertDialog.value = true },
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .bounceClick()
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "Add Category", textAlign = TextAlign.Center, color = Color.White
                        )
                    }


                }
            }
        }
    }

    if (openAlertDialog.value) {
        AddCatPopUpScreen(
            onDismissRequest = { openAlertDialog.value = false }, catViewModel = catViewModel
        )
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun AddCatPopUpScreen(
    onDismissRequest: () -> Unit, catViewModel: CategoryViewModel
) {
    val options = mutableStateListOf("Expenses", "Income")
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    val context = LocalContext.current
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




    Dialog(
        onDismissRequest = {
            onDismissRequest()
        }, properties = DialogProperties(
            usePlatformDefaultWidth = false, dismissOnBackPress = true
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { onDismissRequest() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back"
                    )
                }
            }

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
                            painter = painterResource(id = android.R.drawable.ic_input_add),
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
                            onDismissRequest()
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
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryCard(
    category: Category, modifier: Modifier = Modifier,
) {
    val catViewModel: CategoryViewModel = viewModel()

    val dismissState = rememberDismissState()
    if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
        catViewModel.deleteCategory(category)
    }
    if (!dismissState.isDismissed(DismissDirection.StartToEnd)) {


        SwipeToDeleteItem(state = dismissState) {

            Card(
                modifier = modifier, colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ), border = BorderStroke(width = 1.dp, Color.Black)
            ) {

                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically

                ) {

                    Image(
                        painter = rememberAsyncImagePainter(model = category.imageUri),
                        contentDescription = "",
                        modifier = Modifier
                            .size(30.dp, 30.dp)
                            .padding(end = 10.dp)
                    )

                    Column(
                        horizontalAlignment = Alignment.Start, modifier = Modifier
                    ) {
                        Text(
                            text = category.categoryName, fontWeight = FontWeight.SemiBold
                        )


                    }


                }

            }
        }
    }

}

@Composable
fun CategoryList(categoryList: List<Category>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(categoryList) { item: Category ->
            CategoryCard(
                category = item, modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
            )
        }
    }
}




@Preview(showBackground = true)
@Composable
fun CategoryScreenPreview() {
    CategoryScreen(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    )
}
