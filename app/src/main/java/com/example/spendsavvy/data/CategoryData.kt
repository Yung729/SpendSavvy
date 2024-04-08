package com.example.spendsavvy.data

import com.example.spendsavvy.R
import com.example.spendsavvy.State.Category
import com.example.spendsavvy.State.Transactions

class CategoryData {
    fun loadCategory(mode : Int = 2): List<Category> {

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

        if (mode == 0) {
            for (category in categoryData) {
                if (category.isExpenses) {
                    filterCatData.add(category)
                }
            }
        }else if (mode == 1){
            for (category in categoryData) {
                if (!category.isExpenses) {
                    filterCatData.add(category)
                }
            }
        }else if (mode == 2){
            return categoryData
        }

        return filterCatData

    }
}