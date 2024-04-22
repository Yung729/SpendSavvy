package com.example.spendsavvy.db

import android.content.Context
import androidx.room.Room
import com.example.spendsavvy.models.Category

object DatabaseProvider{
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        synchronized(AppDatabase::class) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "category_database"
                ).build()
            }
            return INSTANCE!!
        }
    }


}