package com.example.spendsavvy.model

import androidx.annotation.DrawableRes

data class Category(
    @DrawableRes val imageResourceId: Int,
    val name: String,
    val isExpenses: Boolean
) {
}