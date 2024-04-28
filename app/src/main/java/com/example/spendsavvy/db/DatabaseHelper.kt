package com.example.spendsavvy.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import com.example.spendsavvy.models.Category

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_CATEGORY_TABLE = """
            CREATE TABLE categories (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                imageUri TEXT,
                categoryName TEXT NOT NULL,
                categoryType TEXT NOT NULL
            )
        """
        db.execSQL(CREATE_CATEGORY_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS categories")
        onCreate(db)
    }


    fun addNewCategory(
        imageUri: Uri?,
        categoryName: String,
        categoryType: String
    ) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("imageUri", imageUri.toString())
            put("categoryName", categoryName)
            put("categoryType", categoryType)
        }
        db.insert("categories", null, values)
        db.close()
    }

    fun deleteAllCategories() {
        val db = this.writableDatabase
        db.delete("categories", null, null)
        db.close()
    }

    fun readCategory(): ArrayList<Category> {
        val db = this.readableDatabase
        val cursorCategory: Cursor = db.rawQuery("SELECT * FROM categories", null)
        val categoryList: ArrayList<Category> = ArrayList()
        if (cursorCategory.moveToFirst()) {
            do {
                categoryList.add(
                    Category(
                        imageUri = Uri.parse(cursorCategory.getString(1)),
                        categoryName = cursorCategory.getString(2),
                        categoryType = cursorCategory.getString(3)
                    )
                )
            } while (cursorCategory.moveToNext())
        }
        cursorCategory.close()
        return categoryList
    }

    companion object {
        private const val DATABASE_NAME = "spendsavvy_database.db"
        private const val DATABASE_VERSION = 1
    }
}

