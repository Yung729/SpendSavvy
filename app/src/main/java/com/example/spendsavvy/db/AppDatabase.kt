package com.example.spendsavvy.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.spendsavvy.dao.CategoryDao
import com.example.spendsavvy.models.Category

@Database(entities = [Category::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
}