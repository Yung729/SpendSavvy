package com.example.spendsavvy.models

import androidx.annotation.DrawableRes

data class Category(
    @DrawableRes val imageResourceId: Int,
    val name: String,
    val isExpenses: Boolean
) {
}