package com.example.spendsavvy.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.spendsavvy.components.SwipeToDeleteItem
import com.example.spendsavvy.components.bounceClick
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.navigation.Screen
import com.example.spendsavvy.viewModels.CategoryViewModel

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier, catViewModel: CategoryViewModel,
    navController: NavController
) {


    val options = mutableStateListOf("Expenses", "Income")
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

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
                CategoryList(categoryList = expenseList, catViewModel = catViewModel)
            } else if (selectedIndex == 1) {
                CategoryList(categoryList = incomeList, catViewModel = catViewModel)
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
                        onClick = { navController.navigate(Screen.AddCategory.route) },
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


}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryCard(
    category: Category, modifier: Modifier = Modifier, catViewModel: CategoryViewModel
) {


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
fun CategoryList(
    categoryList: List<Category>,
    modifier: Modifier = Modifier,
    catViewModel: CategoryViewModel
) {
    LazyColumn(modifier = modifier) {
        items(categoryList) { item: Category ->
            CategoryCard(
                category = item, modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                catViewModel = catViewModel
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
            .padding(20.dp),
        catViewModel = viewModel(),
        navController = rememberNavController()
    )
}
