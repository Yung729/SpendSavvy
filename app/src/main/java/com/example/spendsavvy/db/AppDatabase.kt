package com.example.spendsavvy.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.spendsavvy.models.Category

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        // Create tables and initial database structure
        db.execSQL(CREATE_CATEGORY_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database schema upgrades
    }






    companion object {
        private const val DATABASE_NAME = "spendsavvy_database.db"
        private const val DATABASE_VERSION = 1

        private const val CREATE_CATEGORY_TABLE = """
            CREATE TABLE categories (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                categoryName TEXT NOT NULL,
                categoryType TEXT NOT NULL,
                imageUri TEXT
            )
        """
    }
}

