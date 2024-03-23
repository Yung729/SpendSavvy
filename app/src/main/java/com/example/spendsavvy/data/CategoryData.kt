package com.example.spendsavvy.data

import com.example.spendsavvy.R
import com.example.spendsavvy.model.Category

class CategoryData {
    fun loadCategory(): List<Category> {
        return listOf<Category>(
            Category(R.drawable.wallet,"Salary",true),
            Category(R.drawable.wallet,"Transportation",true),
            Category(R.drawable.wallet,"Office Rental",true),
            Category(R.drawable.wallet,"Office Equipment",true),
            Category(R.drawable.wallet,"Utilities",true),
            Category(R.drawable.wallet,"Team Building",true),
            Category(R.drawable.wallet,"Sales",false),
            Category(R.drawable.wallet,"Subscription",false)
        )

    }
}