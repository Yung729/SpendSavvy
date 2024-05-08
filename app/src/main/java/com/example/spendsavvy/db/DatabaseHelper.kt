package com.example.spendsavvy.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import com.example.spendsavvy.models.Bills
import com.example.spendsavvy.models.Cash
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.repo.FirestoreRepository
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
                amount REAL 
            )
        """
        db.execSQL(CREATE_BUDGET_TABLE)

        val CREATE_GOAL_TABLE = """
            CREATE TABLE goal (
                userId TEXT PRIMARY KEY,
                amount REAL 
            )
        """
        db.execSQL(CREATE_GOAL_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS categories")
        db.execSQL("DROP TABLE IF EXISTS transactions")
        db.execSQL("DROP TABLE IF EXISTS budget")
        db.execSQL("DROP TABLE IF EXISTS goal")
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


    private suspend fun getCategoryId(category: Category, userId: String): String {
        return FirestoreRepository().getDocumentId("Categories", userId, category)
    }


    suspend fun addNewCategoryList(
        categories: List<Category>,
        userId: String
    ) {
        val db = this.writableDatabase


        for (category in categories) {
            val categoryId = getCategoryId(category = category, userId = userId)

            val values = ContentValues().apply {


                put("id", categoryId)
                put("imageUri", category.imageUri.toString())
                put("categoryName", category.categoryName)
                put("categoryType", category.categoryType)
                put("userId", userId)
            }
            db.insert("categories", null, values)
        }

        db.close()


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

    private suspend fun getTransactionId(transactions: Transactions, userId: String): String {
        return FirestoreRepository().getDocumentId("Transactions", userId, transactions)
    }


    suspend fun addNewTransactionsList(
        transactions: List<Transactions>, userId: String
    ) {
        val db = this.writableDatabase

        for (transaction in transactions) {

            val transactionId = getTransactionId(transactions = transaction, userId = userId)

            val categoryId = getCategoryId(category = transaction.category, userId = userId)

            val currentDateMillis = transaction.date.time

            val values = ContentValues().apply {


                put("id", transactionId)
                put("amount", transaction.amount)
                put("categoryId", categoryId)
                put("description", transaction.description)
                put("date", currentDateMillis)
                put("transactionType", transaction.transactionType)
                put("userId", userId)
            }
            db.insert("transactions", null, values)
        }

        db.close()


    }

    //------------------------------------------ Bills ----------------------------------------------------------------------

    fun addNewBill(
        billId: String,
        amount: Double,
        categoryId: String,
        description: String,
        selectedDueDate: String,
        selectedDuration: String,
        billsStatus: String,
        userId: String
    ) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("id", billId)
            put("amount", amount)
            put("categoryId", categoryId)
            put("description", description)
            put("selectedDueDate", selectedDueDate)
            put("selectedDuration", selectedDuration)
            put("billsStatus", billsStatus)
            put("userId", userId)
        }
        db.insert("bills", null, values)
        db.close()
    }

    fun updateBill(
        billId: String,
        amount: Double,
        categoryId: String,
        description: String,
        selectedDueDate: String,
        selectedDuration: String,
        billsStatus: String,
        userId: String
    ) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("id", billId)
            put("amount", amount)
            put("categoryId", categoryId)
            put("description", description)
            put("selectedDueDate", selectedDueDate)
            put("selectedDuration", selectedDuration)
            put("billsStatus", billsStatus)
            put("userId", userId)
        }
        db.update("bills", values, "id=? AND userId=?", arrayOf(billId, userId))
        db.close()
    }

    fun deleteAllBills() {
        val db = this.writableDatabase
        db.delete("bills", null, null)
        db.close()
    }

    fun deleteBill(billId: String, userId: String) {
        val db = this.writableDatabase
        db.delete("bills", "id=? AND userId=?", arrayOf(billId, userId))
        db.close()
    }

    fun readBills(userId: String): ArrayList<Bills> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM bills WHERE userId=?", arrayOf(userId))
        val billsList: ArrayList<Bills> = ArrayList()

        val amountIndex = cursor.getColumnIndex("amount")
        val categoryIdIndex = cursor.getColumnIndex("categoryId")
        val descriptionIndex = cursor.getColumnIndex("description")
        val selectedDueDateIndex = cursor.getColumnIndex("selectedDueDate")
        val selectedDurationIndex = cursor.getColumnIndex("selectedDuration")
        val billsStatusIndex = cursor.getColumnIndex("billsStatus")

        if (cursor.moveToFirst()) {
            do {
                val amount = cursor.getDouble(amountIndex)
                val categoryId = cursor.getString(categoryIdIndex)
                val description = cursor.getString(descriptionIndex)
                val selectedDueDate = cursor.getString(selectedDueDateIndex)
                val selectedDuration = cursor.getString(selectedDurationIndex)
                val billsStatus = cursor.getString(billsStatusIndex)


                // Get the category linked with this bill
                val category = getCategoryById(categoryId)

                // Create a Bills object and add it to the list
                val bill = Bills(amount, category, description,selectedDueDate, selectedDuration, billsStatus)
                billsList.add(bill)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return billsList
    }

    private suspend fun getBillId(bills: Bills, userId: String): String {
        return FirestoreRepository().getDocumentId("Bills", userId, bills)
    }

    suspend fun addNewBillsList(
        bills: List<Bills>, userId: String
    ) {
        val db = this.writableDatabase

        for (bill in bills) {
            val billId = getBillId(bills = bill, userId = userId)
            val categoryId = getCategoryId(category = bill.category, userId = userId)
            val values = ContentValues().apply {
                put("id", billId)
                put("amount", bill.amount)
                put("categoryId", categoryId)
                put("description", bill.description)
                put("selectedDueDate", bill.selectedDueDate)
                put("selectedDuration", bill.selectedDuration)
                put("billsStatus", bill.billsStatus)
                put("userId", userId)
            }
            db.insert("bills", null, values)
        }
        db.close()
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
//------------------------------------------    Goal   ----------------------------------------------------------------------


    fun addOrUpdateGoal(userId: String, amount: Double) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("userId", userId)
            put("amount", amount)
        }
        val result =
            db.insertWithOnConflict("goal", null, values, SQLiteDatabase.CONFLICT_REPLACE)
        if (result == -1L) {
            updateGoal(userId, amount)
        }
        db.close()
    }

    private fun updateGoal(userId: String, amount: Double) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("amount", amount)
        }
        db.update("goal", values, "userId=?", arrayOf(userId))
        db.close()
    }

    fun getGoal(userId: String): Double {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT amount FROM goal WHERE userId=?", arrayOf(userId))
        var goal = 0.0

        val amountIndex = cursor.getColumnIndex("amount")

        if (cursor.moveToFirst()) {
            goal = cursor.getDouble(amountIndex)
        }
        cursor.close()
        return goal
    }

    fun resetGoal(userId: String) {
        updateGoal(userId, 0.0)
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
    fun readCashDetails(userId: String): ArrayList<Cash> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM cash WHERE userId=?", arrayOf(userId))
        val cashDetailsList: ArrayList<Cash> = ArrayList()

        val typeIndex = cursor.getColumnIndex("type")
        val typeNameIndex = cursor.getColumnIndex("typeName")
        val balanceIndex = cursor.getColumnIndex("balance")

        if (cursor.moveToFirst()) {
            do {
                val type = cursor.getString(typeIndex)
                val typeName = cursor.getString(typeNameIndex)
                val balance = cursor.getDouble(balanceIndex)

                val cashDetails = Cash(type, typeName, balance)
                cashDetailsList.add(cashDetails)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return cashDetailsList
    }

    fun addNewCashDetails(
        type: String,
        typeName: String,
        balance: Double,
        userId: String
    ){
        val db = this.writableDatabase
        val values = ContentValues().apply{
            put("type", type)
            put("typeName", typeName)
            put("balance", balance)
            put("userId", userId)
        }

        db.insert("cash", null, values)
        db.close()
    }

    fun addNewCashDetailsList(
        cash: List<Cash>,
        userId: String
    ) {
        val db = this.writableDatabase

        for(cashDetails in cash) {
            val values = ContentValues().apply {
                put("type", cashDetails.type)
                put("typeName", cashDetails.typeName)
                put("balance", cashDetails.balance)
                put("userId", userId)
            }

            db.insert("cash", null, values)
        }
        db.close()
    }

    fun updateCashDetails(
        type: String,
        typeName: String,
        balance: Double,
        userId: String
    ) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("type", type)
            put("typeName", typeName)
            put("balance", balance)
            put("userId", userId)
        }
        db.update("cash", values, "userId=?", arrayOf(userId))
        db.close()
    }
    //----------------------------------- Other ------------------------------------------------------

    fun resetPrimaryKey(tableName: String) {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='$tableName'")
        db.execSQL("VACUUM")
        db.close()
    }

    fun isDatabaseEmpty(): Boolean {
        val db = this.readableDatabase
        var isEmpty = true

        // Check if the categories table has any records
        val categoriesCursor = db.rawQuery("SELECT COUNT(*) FROM categories", null)
        categoriesCursor.use { cursor ->
            if (cursor.moveToFirst()) {
                val count = cursor.getInt(0)
                isEmpty = count == 0
            }
        }

        // Check if the transactions table has any records
        val transactionsCursor = db.rawQuery("SELECT COUNT(*) FROM transactions", null)
        transactionsCursor.use { cursor ->
            if (cursor.moveToFirst()) {
                val count = cursor.getInt(0)
                isEmpty = isEmpty && (count == 0)
            }
        }

        // Check if the budget table has any records
        val budgetCursor = db.rawQuery("SELECT COUNT(*) FROM budget", null)
        budgetCursor.use { cursor ->
            if (cursor.moveToFirst()) {
                val count = cursor.getInt(0)
                isEmpty = isEmpty && (count == 0)
            }
        }

        // Check if the goal table has any records
        val goalCursor = db.rawQuery("SELECT COUNT(*) FROM goal", null)
        goalCursor.use { cursor ->
            if (cursor.moveToFirst()) {
                val count = cursor.getInt(0)
                isEmpty = isEmpty && (count == 0)
            }
        }

        // Optionally, you can check other tables as well if needed

        db.close()

        return isEmpty
    }


    companion object {
        private const val DATABASE_NAME = "Local_Database"
        private const val DATABASE_VERSION = 15
    }
}