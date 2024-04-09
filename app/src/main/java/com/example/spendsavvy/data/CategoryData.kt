package com.example.spendsavvy.data

import com.example.spendsavvy.R
import com.example.spendsavvy.models.Category

class CategoryData {
    fun loadCategory(mode : Int = 2): List<Category> {

        val filterCatData = mutableListOf<Category>()

        val categoryData = listOf(
            Category(imageResourceId = R.drawable.wallet, categoryName = "Salary", isExpenses = true),
            Category(imageResourceId = R.drawable.wallet, categoryName = "Salary", isExpenses = true),
            Category(imageResourceId = R.drawable.wallet, categoryName = "Salary", isExpenses = true),
            Category(imageResourceId = R.drawable.wallet, categoryName = "Salary", isExpenses = true),
            Category(imageResourceId = R.drawable.wallet, categoryName = "Salary", isExpenses = true),
            Category(imageResourceId = R.drawable.wallet, categoryName = "Salary", isExpenses = true),
            Category(imageResourceId = R.drawable.wallet, categoryName = "Salary", isExpenses = true),
            Category(imageResourceId = R.drawable.wallet, categoryName = "Salary", isExpenses = true),
            Category(imageResourceId = R.drawable.wallet,categoryName = "Sales",isExpenses = false),
            Category(imageResourceId = R.drawable.wallet,categoryName = "Sales",isExpenses = false)
        )

        when (mode) {
            0 -> {
                for (category in categoryData) {
                    if (category.isExpenses) {
                        filterCatData.add(category)
                    }
                }
            }
            1 -> {
                for (category in categoryData) {
                    if (!category.isExpenses) {
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