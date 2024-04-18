package com.example.spendsavvy.data

import com.example.spendsavvy.R
import com.example.spendsavvy.models.Category

class CategoryData {
    fun loadCategory(mode : Int = 2): List<Category> {

        val filterCatData = mutableListOf<Category>()

        val categoryData = listOf(
            Category( categoryName = "Salary", categoryType = "Expenses"),
            Category( categoryName = "Salary", categoryType = "Expenses"),
            Category( categoryName = "Salary", categoryType = "Expenses"),
            Category( categoryName = "Salary", categoryType = "Expenses"),
            Category( categoryName = "Salary", categoryType = "Expenses"),
            Category( categoryName = "Salary", categoryType = "Expenses"),
            Category( categoryName = "Salary", categoryType = "Expenses"),
            Category( categoryName = "Salary", categoryType = "Expenses"),
            Category(categoryName = "Sales",categoryType = "Incomes"),
            Category(categoryName = "Sales",categoryType = "Incomes")
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