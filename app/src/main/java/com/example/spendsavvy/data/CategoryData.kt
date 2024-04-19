package com.example.spendsavvy.data

import android.net.Uri
import com.example.spendsavvy.R
import com.example.spendsavvy.models.Category

class CategoryData{

    fun drawableToUri(drawableId: Int): Uri {
        return Uri.parse("android.resource://com.example.spendsavvy/$drawableId")
    }

    fun loadCategory(mode: Int = 2): List<Category> {
        val filterCatData = mutableListOf<Category>()

        val categoryData = listOf(
            Category(
                imageUri = drawableToUri(drawableId = R.drawable.eletrical),
                categoryName = "Electrical",
                categoryType = "Expenses"
            ),
            Category(
                imageUri = drawableToUri(drawableId = R.drawable.stock),
                categoryName = "Stock Sales",
                categoryType = "Incomes"
            ),
            Category(
                imageUri = drawableToUri(drawableId = R.drawable.consult),
                categoryName = "Consultation Fee",
                categoryType = "Incomes"
            ),

            )

        when (mode) {
            0 -> {
                for (category in categoryData) {
                    if (category.categoryType == "Expenses") {
                        filterCatData.add(category)
                    }
                }
            }

            1 -> {
                for (category in categoryData) {
                    if (category.categoryType == "Incomes") {
                        filterCatData.add(category)
                    }
                }
            }

            2 -> {
                return categoryData
            }
        }

        return filterCatData

    }
}