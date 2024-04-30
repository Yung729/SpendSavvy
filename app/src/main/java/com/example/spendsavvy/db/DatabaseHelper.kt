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
                id TEXT PRIMARY KEY,
                imageUri TEXT,
                categoryName TEXT NOT NULL,
                categoryType TEXT NOT NULL,
                userId TEXT
            )
        """
        db.execSQL(CREATE_CATEGORY_TABLE)

        val CREATE_TRANSACTION_TABLE = """
            CREATE TABLE transactions (
                id TEXT PRIMARY KEY,
                amount REAL NOT NULL,
                categoryId TEXT,
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
        categoryId: String,
        imageUri: Uri?,
        categoryName: String,
        categoryType: String,
        userId: String
    ) {

        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("id", categoryId)
            put("imageUri", imageUri.toString())
            put("categoryName", categoryName)
            put("categoryType", categoryType)
            put("userId", userId)
        }
        db.insert("categories", null, values)
        db.close()
    }

    fun updateCategory(
        categoryId: String,
        imageUri: Uri?,
        categoryName: String,
        categoryType: String,
        userId: String
    ) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("id", categoryId)
            put("imageUri", imageUri.toString())
            put("categoryName", categoryName)
            put("categoryType", categoryType)
            put("userId", userId)
        }
        db.update("categories", values, "id=? AND userId=?", arrayOf(categoryId, userId))
        db.close()
    }

    fun deleteCategory(categoryId: String, userId: String) {
        val db = this.writableDatabase
        db.delete("categories", "id=? AND userId=?", arrayOf(categoryId, userId))
        db.close()
    }


    fun deleteAllCategories() {
        val db = this.writableDatabase
        db.delete("categories", null, null)
        db.close()
    }

    private fun getCategoryById(categoryId: String): Category {
        val db = this.readableDatabase
        val cursor =
            db.rawQuery("SELECT * FROM categories WHERE id=?", arrayOf(categoryId))
        cursor.moveToFirst()
        val imageUri = Uri.parse(cursor.getString(1))
        val categoryName = cursor.getString(2)
        val categoryType = cursor.getString(3)
        cursor.close()
        return Category(imageUri, categoryName, categoryType)
    }

    fun getCategoryId(categoryName: String): String {
        val db = this.readableDatabase
        val cursor =
            db.rawQuery("SELECT * FROM categories WHERE categoryName=?", arrayOf(categoryName))
        cursor.moveToFirst()
        val categoryId = cursor.getString(0) // Return the category row id
        cursor.close()
        return categoryId
    }

    fun readCategory(userId: String): ArrayList<Category> {
        val db = this.readableDatabase
        val cursorCategory: Cursor =
            db.rawQuery("SELECT * FROM categories WHERE userId=?", arrayOf(userId))
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
        transactionId: String,
        amount: Double,
        categoryId: String,  // Category ID to link the transaction with a category
        description: String,
        date: Date,
        transactionType: String,
        userId: String
    ) {
        val currentDateMillis = date.time
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("id", transactionId)
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
        transactionId: String,
        amount: Double,
        categoryId: String,
        description: String,
        date: Date,
        transactionType: String,
        userId: String
    ) {
        val currentDateMillis = date.time
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("id", transactionId)
            put("amount", amount)
            put("categoryId", categoryId)
            put("description", description)
            put("date", currentDateMillis)
            put("transactionType", transactionType)
            put("userId", userId)
        }
        db.update("transactions", values, "id=? AND userId=?", arrayOf(transactionId, userId))
        db.close()
    }

    fun deleteAllTransaction() {
        val db = this.writableDatabase
        db.delete("transactions", null, null)
        db.close()
    }

    fun deleteTransaction(transactionId: String, userId: String) {
        val db = this.writableDatabase
        db.delete("transactions", "id=? AND userId=?", arrayOf(transactionId, userId))
        db.close()
    }

    fun readTransactions(userId: String): ArrayList<Transactions> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM transactions WHERE userId=?", arrayOf(userId))
        val transactionList: ArrayList<Transactions> = ArrayList()
        if (cursor.moveToFirst()) {
            do {
                val amount = cursor.getDouble(1)
                val categoryId = cursor.getString(2)
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
        private const val DATABASE_VERSION = 6
    }
}

