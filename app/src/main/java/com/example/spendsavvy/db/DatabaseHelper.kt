package com.example.spendsavvy.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.Transactions
import java.util.Date

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {

        val CREATE_CATEGORY_TABLE = """
            CREATE TABLE categories (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                imageUri TEXT,
                categoryName TEXT NOT NULL,
                categoryType TEXT NOT NULL,
                userId TEXT
            )
        """
        db.execSQL(CREATE_CATEGORY_TABLE)

        val CREATE_TRANSACTION_TABLE = """
            CREATE TABLE transactions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                amount REAL NOT NULL,
                categoryId INTEGER,
                description TEXT,
                date LONG,
                transactionType TEXT,
                userId TEXT,
                FOREIGN KEY(categoryId) REFERENCES categories(id)
            )
        """
        db.execSQL(CREATE_TRANSACTION_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS categories")
        db.execSQL("DROP TABLE IF EXISTS transactions")
        onCreate(db)
    }


    fun addNewCategory(
        imageUri: Uri?,
        categoryName: String,
        categoryType: String,
        userId : String
    ) {

        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("imageUri", imageUri.toString())
            put("categoryName", categoryName)
            put("categoryType", categoryType)
            put("userId", userId)
        }
        db.insert("categories", null, values)
        db.close()
    }

    fun updateCategory(
        categoryId: Long,
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
        db.update("categories", values, "id=?", arrayOf(categoryId.toString()))
        db.close()
    }

    fun deleteCategory(categoryId: Long) {
        val db = this.writableDatabase
        db.delete("categories", "id=?", arrayOf(categoryId.toString()))
        db.close()
    }


    fun deleteAllCategories() {
        val db = this.writableDatabase
        db.delete("categories", null, null)
        db.close()
    }

    private fun getCategoryById(categoryId: Long): Category {
        val db = this.readableDatabase
        val cursor =
            db.rawQuery("SELECT * FROM categories WHERE id=?", arrayOf(categoryId.toString()))
        cursor.moveToFirst()
        val imageUri = Uri.parse(cursor.getString(1))
        val categoryName = cursor.getString(2)
        val categoryType = cursor.getString(3)
        cursor.close()
        return Category(imageUri, categoryName, categoryType)
    }

    fun getCategoryId(categoryName: String): Long {
        val db = this.readableDatabase
        val cursor =
            db.rawQuery("SELECT * FROM categories WHERE categoryName=?", arrayOf(categoryName))
        cursor.moveToFirst()
        val categoryId = cursor.getLong(0) // Return the category row id
        cursor.close()
        return categoryId
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

//------------------------------------------    Transaction ----------------------------------------------------------------------

    fun addNewTransaction(
        amount: Double,
        categoryId: Long,  // Category ID to link the transaction with a category
        description: String,
        date: Date,
        transactionType: String,
        userId: String
    ) {
        val currentDateMillis = date.time
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("amount", amount)
            put("categoryId", categoryId)
            put("description", description)
            put("date", currentDateMillis)
            put("transactionType", transactionType)
            put("userId", userId)
        }
        db.insert("transactions", null, values)
        db.close()
    }

    fun updateTransaction(
        transactionId: Long,
        amount: Double,
        categoryId: Long,
        description: String,
        date: Date,
        transactionType: String
    ) {
        val currentDateMillis = date.time
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("amount", amount)
            put("categoryId", categoryId)
            put("description", description)
            put("date", currentDateMillis)
            put("transactionType", transactionType)
        }
        db.update("transactions", values, "id=?", arrayOf(transactionId.toString()))
        db.close()
    }

    fun deleteAllTransaction() {
        val db = this.writableDatabase
        db.delete("transactions", null, null)
        db.close()
    }

    fun deleteTransaction(transactionId: Long) {
        val db = this.writableDatabase
        db.delete("transactions", "id=?", arrayOf(transactionId.toString()))
        db.close()
    }

    fun readTransactions(): ArrayList<Transactions> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM transactions", null)
        val transactionList: ArrayList<Transactions> = ArrayList()
        if (cursor.moveToFirst()) {
            do {
                val amount = cursor.getDouble(1)
                val categoryId = cursor.getLong(2)
                val description = cursor.getString(3)
                val dateMillis = cursor.getLong(4)
                val transactionType = cursor.getString(5)

                val date = Date(dateMillis)

                // Get the category linked with this transaction
                val category = getCategoryById(categoryId)

                // Create a Transaction object and add it to the list
                val transaction = Transactions(amount, category, description, date, transactionType)
                transactionList.add(transaction)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return transactionList
    }


    fun resetPrimaryKey(tableName: String) {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='$tableName'")
        db.execSQL("VACUUM")
        db.close()
    }


    companion object {
        private const val DATABASE_NAME = "Local_Database"
        private const val DATABASE_VERSION = 5
    }
}

