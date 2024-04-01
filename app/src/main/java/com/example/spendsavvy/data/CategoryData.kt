package com.example.spendsavvy.data

import com.example.spendsavvy.R
import com.example.spendsavvy.model.Category
import com.example.spendsavvy.model.Transactions

class CategoryData {
    fun loadCategory(mode : Int): List<Category> {

        val filterCatData = mutableListOf<Category>()

        val categoryData = listOf(
            Category(R.drawable.wallet,"Salary",true),
            Category(R.drawable.wallet,"Transportation",true),
            Category(R.drawable.wallet,"Office Rental",true),
            Category(R.drawable.wallet,"Office Equipment",true),
            Category(R.drawable.wallet,"Utilities",true),
            Category(R.drawable.wallet,"Team Building",true),
            Category(R.drawable.wallet,"Sales",false),
            Category(R.drawable.wallet,"Subscription",false)
        )

        if (mode == 1) {
            for (category in categoryData) {
                if (category.isExpenses) {
                    filterCatData.add(category)
                }
            }
        }else if (mode == 2){
            for (category in categoryData) {
                if (!category.isExpenses) {
                    filterCatData.add(category)
                }
            }
        }

        return filterCatData

    }
}