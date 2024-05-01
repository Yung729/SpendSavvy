package com.example.spendsavvy.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import com.example.spendsavvy.models.Cash
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.Stock
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

        val CREATE_BUDGET_TABLE = """
            CREATE TABLE budget (
                userId TEXT PRIMARY KEY,
                amount REAL NOT NULL
            )
        """
        db.execSQL(CREATE_BUDGET_TABLE)

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
        val cursor = db.rawQuery("SELECT * FROM categories WHERE id=?", arrayOf(categoryId))
        cursor.moveToFirst()
        val imageUri = Uri.parse(cursor.getString(1))
        val categoryName = cursor.getString(2)
        val categoryType = cursor.getString(3)
        cursor.close()
        return Category(imageUri, categoryName, categoryType)
    }

    fun readCategory(userId: String): ArrayList<Category> {
        val db = this.readableDatabase
        val cursorCategory: Cursor =
            db.rawQuery("SELECT * FROM categories WHERE userId=?", arrayOf(userId))
        val categoryList: ArrayList<Category> = ArrayList()

        val imageUriIndex = cursorCategory.getColumnIndex("imageUri")
        val categoryNameIndex = cursorCategory.getColumnIndex("categoryName")
        val categoryTypeIndex = cursorCategory.getColumnIndex("categoryType")

        if (cursorCategory.moveToFirst()) {
            do {
                categoryList.add(
                    Category(
                        imageUri = Uri.parse(cursorCategory.getString(imageUriIndex)),
                        categoryName = cursorCategory.getString(categoryNameIndex),
                        categoryType = cursorCategory.getString(categoryTypeIndex)
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

        val amountIndex = cursor.getColumnIndex("amount")
        val categoryIdIndex = cursor.getColumnIndex("categoryId")
        val descriptionIndex = cursor.getColumnIndex("description")
        val dateIndex = cursor.getColumnIndex("date")
        val transactionTypeIndex = cursor.getColumnIndex("transactionType")

        if (cursor.moveToFirst()) {


            do {
                val amount = cursor.getDouble(amountIndex)
                val categoryId = cursor.getString(categoryIdIndex)
                val description = cursor.getString(descriptionIndex)
                val dateMillis = cursor.getLong(dateIndex)
                val transactionType = cursor.getString(transactionTypeIndex)

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

//------------------------------------------    Budget   ----------------------------------------------------------------------


    fun addOrUpdateBudget(userId: String, amount: Double) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("userId", userId)
            put("amount", amount)
        }
        val result =
            db.insertWithOnConflict("budget", null, values, SQLiteDatabase.CONFLICT_REPLACE)
        if (result == -1L) {
            updateBudget(userId, amount)
        }
        db.close()
    }

    private fun updateBudget(userId: String, amount: Double) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("amount", amount)
        }
        db.update("budget", values, "userId=?", arrayOf(userId))
        db.close()
    }

    fun getBudget(userId: String): Double {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT amount FROM budget WHERE userId=?", arrayOf(userId))
        var budget = 0.0

        val amountIndex = cursor.getColumnIndex("amount")

        if (cursor.moveToFirst()) {
            budget = cursor.getDouble(amountIndex)
        }
        cursor.close()
        return budget
    }

    fun resetBudget(userId: String) {
        updateBudget(userId, 0.0)
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


//------------------------------ CASH ----------------------------------------
    fun readCashDetails(): ArrayList<Cash> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM cash", null)
        val cashAccountList: ArrayList<Cash> = ArrayList()
        if (cursor.moveToFirst()) {
            do {
                val balance = cursor.getDouble(1)

                // Create a Cash object and add it to the list
                val cashAccount = Cash(balance)
                cashAccountList.add(cashAccount)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return  cashAccountList
    }

    fun addNewCashDetails(
        balance: Double
    ) {

        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("balance", balance)
        }
        db.insert("cash", null, values)
        db.close()
    }

    fun updateCashDetails(
        balance: Double,
        userId: String
    ) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("balance", balance)
        }
        db.update("cash", values, "userId=?", arrayOf(userId))
        db.close()
    }



    fun resetPrimaryKey(tableName: String) {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='$tableName'")
        db.execSQL("VACUUM")
        db.close()
    }


    companion object {
        private const val DATABASE_NAME = "Local_Database"
        private const val DATABASE_VERSION = 9
    }
}

