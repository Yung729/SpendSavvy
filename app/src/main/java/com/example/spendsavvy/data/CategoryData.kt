package com.example.spendsavvy.data

import com.example.spendsavvy.model.Category

class CategoryData {
    fun loadCategory(): List<Category> {
        return listOf<Category>(
            Category("Salary",true),
            Category("Transportation",true),
            Category("Office Rental",true),
            Category("Office Equipment",true),
            Category("Utilities",true),
            Category("Team Building",true),
            Category("Sales",false),
            Category("Subscription",false)
        )

    }
}