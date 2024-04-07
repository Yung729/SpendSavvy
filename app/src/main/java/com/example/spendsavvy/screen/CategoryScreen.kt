package com.example.spendsavvy.screen

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.spendsavvy.animation.bounceClick
import com.example.spendsavvy.data.CategoryData
import com.example.spendsavvy.model.Category

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState", "RememberReturnType")
@Composable
fun CategoryScreen(modifier: Modifier = Modifier) {

    val options = mutableStateListOf("Expenses", "Income")
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }
    val openAlertDialog = remember { mutableStateOf(false) }



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

            CategoryList(categoryList = CategoryData().loadCategory(selectedIndex))

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

                    Button(
                        onClick = {},
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .bounceClick()
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Remove Category",
                            textAlign = TextAlign.Center,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }

    if (openAlertDialog.value) {
        AddCatPopUpScreen(onDismissRequest = { openAlertDialog.value = false })
    }


}

@Composable
fun AddCatPopUpScreen(
    onDismissRequest: () -> Unit
) {

    var categoryState by remember { mutableStateOf(TextFieldValue()) }
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val photoPickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(),
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
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onDismissRequest() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back"
                        )
                    }
                }

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
                        containerColor = Color(0xFF0073E6), contentColor = Color.White
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

                Spacer(modifier = Modifier.height(8.dp))


                OutlinedTextField(
                    value = categoryState,
                    onValueChange = { categoryState = it },
                    label = { Text(text = "Category Name") },
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp, top = 10.dp),
                    shape = RoundedCornerShape(15.dp),
                    singleLine = true,
                )

                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    model = selectedImageUri,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )

            }
        }
    }
}

@Composable
fun CategoryCard(category: Category, modifier: Modifier = Modifier) {

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
                painter = painterResource(id = category.imageResourceId),
                contentDescription = "",
                modifier = Modifier
                    .size(30.dp, 30.dp)
                    .padding(end = 10.dp)
            )

            Column(
                horizontalAlignment = Alignment.Start, modifier = Modifier
            ) {
                Text(
                    text = category.name, fontWeight = FontWeight.SemiBold
                )


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
        Modifier
            .fillMaxSize()
            .padding(20.dp)
    )
}