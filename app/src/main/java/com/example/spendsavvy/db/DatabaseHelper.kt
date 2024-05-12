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
import com.example.spendsavvy.models.Question
import com.example.spendsavvy.models.Staff
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.repo.FirestoreRepository
import java.util.Date

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {

        val CREATE_CATEGORY_TABLE = """
            CREATE TABLE categories (
                id TEXT PRIMARY KEY,
                internalId TEXT,
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
                internalId TEXT,
                amount REAL NOT NULL,
                categoryId TEXT,
                paymentMethod TEXT,
                description TEXT,
                date LONG,
                transactionType TEXT,
                userId TEXT,
                FOREIGN KEY(categoryId) REFERENCES categories(id)
            )
        """
        db.execSQL(CREATE_TRANSACTION_TABLE)

        val CREATE_STAFF_TABLE = """
            CREATE TABLE staff (
                id TEXT PRIMARY KEY,
                staffIC TEXT,
                staffName TEXT,
                salary REAL,
                userId TEXT
            )
        """
        db.execSQL(CREATE_STAFF_TABLE)

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

        val CREATE_BILL_TABLE = """
            CREATE TABLE bills (
                id TEXT PRIMARY KEY,
                internalId TEXT,
                amount REAL NOT NULL,
                categoryId TEXT,
                description TEXT,
                selectedDueDate LONG,
                selectedDuration TEXT,
                billsStatus TEXT,
                userId TEXT,
                FOREIGN KEY(categoryId) REFERENCES categories(id)
            )
        """
        db.execSQL(CREATE_BILL_TABLE)

        val CREATE_QUESTION_TABLE = """
            CREATE TABLE questions (
                id TEXT PRIMARY KEY,
                internalId TEXT,
                questionText TEXT,
                answer TEXT,
                status TEXT,
                date LONG,
                userId TEXT
            )
        """
        db.execSQL(CREATE_QUESTION_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS categories")
        db.execSQL("DROP TABLE IF EXISTS transactions")
        db.execSQL("DROP TABLE IF EXISTS budget")
        db.execSQL("DROP TABLE IF EXISTS goal")
        db.execSQL("DROP TABLE IF EXISTS staff")
        db.execSQL("DROP TABLE IF EXISTS bills")
        db.execSQL("DROP TABLE IF EXISTS questions")
        onCreate(db)
    }

    fun addNewCategory(
        categoryId: String,
        internalId: String,
        imageUri: Uri?,
        categoryName: String,
        categoryType: String,
        userId: String
    ) {

        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("id", categoryId)
            put("internalId", internalId)
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
        internalId: String,
        imageUri: Uri?,
        categoryName: String,
        categoryType: String,
        userId: String
    ) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("id", categoryId)
            put("internalId", internalId)
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

        val internalIdIndex = cursor.getColumnIndex("internalId")
        val imageUriIndex = cursor.getColumnIndex("imageUri")
        val categoryNameIndex = cursor.getColumnIndex("categoryName")
        val categoryTypeIndex = cursor.getColumnIndex("categoryType")

        cursor.moveToFirst()
        val internalId = cursor.getString(internalIdIndex)
        val imageUri = Uri.parse(cursor.getString(imageUriIndex))
        val categoryName = cursor.getString(categoryNameIndex)
        val categoryType = cursor.getString(categoryTypeIndex)
        cursor.close()
        return Category(internalId,imageUri, categoryName, categoryType)
    }

    fun readCategory(userId: String): ArrayList<Category> {
        val db = this.readableDatabase
        val cursorCategory: Cursor =
            db.rawQuery("SELECT * FROM categories WHERE userId=?", arrayOf(userId))
        val categoryList: ArrayList<Category> = ArrayList()

        val internalIdIndex = cursorCategory.getColumnIndex("internalId")
        val imageUriIndex = cursorCategory.getColumnIndex("imageUri")
        val categoryNameIndex = cursorCategory.getColumnIndex("categoryName")
        val categoryTypeIndex = cursorCategory.getColumnIndex("categoryType")

        if (cursorCategory.moveToFirst()) {
            do {
                categoryList.add(
                    Category(
                        id = cursorCategory.getString(internalIdIndex),
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
                put("internalId", category.id)
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
        internalId: String,
        amount: Double,
        categoryId: String,  // Category ID to link the transaction with a category
        paymentMethod : String,
        description: String,
        date: Date,
        transactionType: String,
        userId: String
    ) {
        val currentDateMillis = date.time
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("id", transactionId)
            put("internalId", internalId)
            put("amount", amount)
            put("categoryId", categoryId)
            put("paymentMethod", paymentMethod)
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
        internalId: String,
        amount: Double,
        categoryId: String,
        paymentMethod : String,
        description: String,
        date: Date,
        transactionType: String,
        userId: String
    ) {
        val currentDateMillis = date.time
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("id", transactionId)
            put("internalId", internalId)
            put("amount", amount)
            put("categoryId", categoryId)
            put("paymentMethod", paymentMethod)
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

        val internalIdIndex = cursor.getColumnIndex("internalId")
        val amountIndex = cursor.getColumnIndex("amount")
        val categoryIdIndex = cursor.getColumnIndex("categoryId")
        val paymentMethodIndex = cursor.getColumnIndex("paymentMethod")
        val descriptionIndex = cursor.getColumnIndex("description")
        val dateIndex = cursor.getColumnIndex("date")
        val transactionTypeIndex = cursor.getColumnIndex("transactionType")

        if (cursor.moveToFirst()) {


            do {
                val internalId = cursor.getString(internalIdIndex)
                val amount = cursor.getDouble(amountIndex)
                val categoryId = cursor.getString(categoryIdIndex)
                val paymentMethod = cursor.getString(paymentMethodIndex)
                val description = cursor.getString(descriptionIndex)
                val dateMillis = cursor.getLong(dateIndex)
                val transactionType = cursor.getString(transactionTypeIndex)

                val date = Date(dateMillis)

                // Get the category linked with this transaction
                val category = getCategoryById(categoryId)

                // Create a Transaction object and add it to the list
                val transaction = Transactions(internalId,amount, category,paymentMethod, description, date, transactionType)
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
                put("internalId", transaction.id)
                put("amount", transaction.amount)
                put("categoryId", categoryId)
                put("paymentMethod", transaction.paymentMethod)
                put("description", transaction.description)
                put("date", currentDateMillis)
                put("transactionType", transaction.transactionType)
                put("userId", userId)
            }
            db.insert("transactions", null, values)
        }

        db.close()


    }

    fun getTransactionByCategoryName(userId: String, categoryName: String): List<Transactions> {
        val db = this.readableDatabase
        val transactions = mutableListOf<Transactions>()

        val query = "SELECT * FROM transactions WHERE userId=? AND categoryId IN " +
                "(SELECT id FROM categories WHERE categoryName=?)"

        val cursor = db.rawQuery(query, arrayOf(userId, categoryName))

        val internalIdIndex = cursor.getColumnIndex("internalId")
        val amountIndex = cursor.getColumnIndex("amount")
        val categoryIdIndex = cursor.getColumnIndex("categoryId")
        val paymentMethodIndex = cursor.getColumnIndex("paymentMethod")
        val descriptionIndex = cursor.getColumnIndex("description")
        val dateIndex = cursor.getColumnIndex("date")
        val transactionTypeIndex = cursor.getColumnIndex("transactionType")

        while (cursor.moveToNext()) {
            val internalId = cursor.getString(internalIdIndex)
            val amount = cursor.getDouble(amountIndex)
            val categoryId = cursor.getString(categoryIdIndex)
            val paymentMethod = cursor.getString(paymentMethodIndex)
            val description = cursor.getString(descriptionIndex)
            val dateMillis = cursor.getLong(dateIndex)
            val transactionType = cursor.getString(transactionTypeIndex)

            val date = Date(dateMillis)

            // Get the category linked with this transaction
            val category = getCategoryById(categoryId)

            // Create a Transaction object and add it to the list
            val transaction = Transactions(internalId, amount, category,paymentMethod, description, date, transactionType)
            transactions.add(transaction)
        }

        cursor.close()
        db.close()

        return transactions
    }

    //------------------------------------------ Bills ----------------------------------------------------------------------

    fun addNewBill(
        billId: String,
        amount: Double,
        categoryId: String,
        description: String,
        selectedDueDate: Date,
        selectedDuration: String,
        billsStatus: String,
        userId: String
    ) {
        val selectedDueDateMillis = selectedDueDate.time

        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("internalId", billId)
            put("amount", amount)
            put("categoryId", categoryId)
            put("description", description)
            put("selectedDueDate", selectedDueDateMillis)
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
        selectedDueDate: Date,
        selectedDuration: String,
        billsStatus: String,
        userId: String
    ) {
        val selectedDueDateMillis = selectedDueDate.time
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("internalId", billId)
            put("amount", amount)
            put("categoryId", categoryId)
            put("description", description)
            put("selectedDueDate", selectedDueDateMillis)
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

        val internalIdIndex = cursor.getColumnIndex("internalId")
        val amountIndex = cursor.getColumnIndex("amount")
        val categoryIdIndex = cursor.getColumnIndex("categoryId")
        val descriptionIndex = cursor.getColumnIndex("description")
        val selectedDueDateIndex = cursor.getColumnIndex("selectedDueDate")
        val selectedDurationIndex = cursor.getColumnIndex("selectedDuration")
        val billsStatusIndex = cursor.getColumnIndex("billsStatus")

        if (cursor.moveToFirst()) {
            do {
                val internalId = cursor.getString(internalIdIndex)
                val amount = cursor.getDouble(amountIndex)
                val categoryId = cursor.getString(categoryIdIndex)
                val description = cursor.getString(descriptionIndex)
                val selectedDueDateMillis = cursor.getLong(selectedDueDateIndex)
                val selectedDuration = cursor.getString(selectedDurationIndex)
                val billsStatus = cursor.getString(billsStatusIndex)

                // Get the category linked with this bill
                val category = getCategoryById(categoryId)
                val selectedDueDate = Date(selectedDueDateMillis)

                // Create a Bills object and add it to the list
                val bill = Bills(internalId,amount, category, description,selectedDueDate, selectedDuration, billsStatus)
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

            // Convert Date to milliseconds
            val currentDateMillis = bill.selectedDueDate.time

            val values = ContentValues().apply {
                put("id", billId)
                put("amount", bill.amount)
                put("categoryId", categoryId)
                put("description", bill.description)
                put("selectedDueDate", currentDateMillis)
                put("selectedDuration", bill.selectedDuration)
                put("billsStatus", bill.billsStatus)
                put("userId", userId)
            }
            db.insert("bills", null, values)
        }
        db.close()
    }
//------------------------------------------    Question   ----------------------------------------------------------------------

    fun addNewQuestion(
        questionId: String,
        questionText: String,
        answer: String?,
        status: String,
        questionDate: Date,
        userId: String
    ) {
        val dateMillis = questionDate.time

        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("internalId", questionId)
            put("questionText", questionText)
            put("answer", answer)
            put("status", status)
            put("questionDate", dateMillis)
            put("userId", userId)
        }
        db.insert("questions", null, values)
        db.close()
    }

    fun updateQuestion(
        questionId: String,
        questionText: String,
        answer: String?,
        status: String,
        questionDate: Date,
        userId: String
    ) {
        val dateMillis = questionDate.time
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("internalId", questionId)
            put("questionText", questionText)
            put("answer", answer)
            put("status", status)
            put("questionDate", dateMillis)
            put("userId", userId)
        }
        db.update("questions", values, "id=? AND userId=?", arrayOf(questionId, userId))
        db.close()
    }

    fun deleteAllQuestions() {
        val db = this.writableDatabase
        db.delete("questions", null, null)
        db.close()
    }

    fun deleteQuestion(questionId: String, userId: String) {
        val db = this.writableDatabase
        db.delete("questions", "id=? AND userId=?", arrayOf(questionId, userId))
        db.close()
    }

    fun readQuestions(userId: String): ArrayList<Question> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM questions WHERE userId=?", arrayOf(userId))
        val questionsList: ArrayList<Question> = ArrayList()

        val internalIdIndex = cursor.getColumnIndex("internalId")
        val questionTextIndex = cursor.getColumnIndex("questionText")
        val answerIndex = cursor.getColumnIndex("answer")
        val statusIndex = cursor.getColumnIndex("status")
        val dateIndex = cursor.getColumnIndex("date")

        if (cursor.moveToFirst()) {
            do {
                val internalId = cursor.getString(internalIdIndex)
                val questionText = cursor.getString(questionTextIndex)
                val answer = cursor.getString(answerIndex)
                val status = cursor.getString(statusIndex)
                val dateMillis = cursor.getLong(dateIndex)
                val date = Date(dateMillis)

                val question = Question(internalId, questionText, answer, status, date)
                questionsList.add(question)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return questionsList
    }

    private suspend fun getQuestionId(question: Question, userId: String): String {
        return FirestoreRepository().getDocumentId("Questions", userId, question)
    }

    suspend fun addNewQuestionsList(
        questions: List<Question>, userId: String
    ) {
        val db = this.writableDatabase

        for (question in questions) {
            val questionId = getQuestionId(question = question, userId = userId)

            // Convert Date to milliseconds
            val dateMillis = question.questionDate.time

            val values = ContentValues().apply {
                put("id", questionId)
                put("questionText", question.questionText)
                put("answer", question.answer)
                put("status", question.status)
                put("questionDate", dateMillis)
                put("userId", userId)
            }
            db.insert("questions", null, values)
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

    //----------------------------- Staff -------------------------------------------------------------------
    fun addNewStaff(
        staffId: String,
        staffIC: String,
        staffName: String,
        staffSalary: Double,
        userId: String
    ) {

        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("id", staffId)
            put("staffIC", staffIC)
            put("staffName", staffName)
            put("salary", staffSalary.toString())
            put("userId", userId)
        }
        db.insert("staff", null, values)
        db.close()
    }

    fun updateStaff(
        staffId: String,
        staffIC: String,
        staffName: String,
        staffSalary: Double,
        userId: String
    ) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("id", staffId)
            put("staffIC", staffIC)
            put("staffName", staffName)
            put("salary", staffSalary.toString())
            put("userId", userId)
        }
        db.update("staff", values, "id=? AND userId=?", arrayOf(staffId, userId))
        db.close()
    }

    fun deleteStaff(staffId: String, userId: String) {
        val db = this.writableDatabase
        db.delete("staff", "id=? AND userId=?", arrayOf(staffId, userId))
        db.close()
    }


    fun deleteAllStaff() {
        val db = this.writableDatabase
        db.delete("staff", null, null)
        db.close()
    }

    private fun getStaffById(staffId: String): Staff {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM staff WHERE id=?", arrayOf(staffId))

        val staffICIndex = cursor.getColumnIndex("staffIC")
        val staffNameIndex = cursor.getColumnIndex("staffName")
        val salaryIndex = cursor.getColumnIndex("salary")

        cursor.moveToFirst()
        val staffIc = cursor.getString(staffICIndex)
        val staffName = cursor.getString(staffNameIndex)
        val staffSalary = cursor.getDouble(salaryIndex)

        cursor.close()
        return Staff(staffIc,staffName, staffSalary)
    }

    fun readStaff(userId: String): ArrayList<Staff> {
        val db = this.readableDatabase
        val cursor: Cursor =
            db.rawQuery("SELECT * FROM staff WHERE userId=?", arrayOf(userId))
        val staffList: ArrayList<Staff> = ArrayList()

        val staffICIndex = cursor.getColumnIndex("staffIC")
        val staffNameIndex = cursor.getColumnIndex("staffName")
        val salaryIndex = cursor.getColumnIndex("salary")

        if (cursor.moveToFirst()) {
            do {
                staffList.add(
                    Staff(
                        id = cursor.getString(staffICIndex),
                        name = cursor.getString(staffNameIndex),
                        salary = cursor.getDouble(salaryIndex)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return staffList
    }


    private suspend fun getStaffId(staff: Staff, userId: String): String {
        return FirestoreRepository().getDocumentId("Staff", userId, staff)
    }


    suspend fun addNewStaffList(
        staff: List<Staff>,
        userId: String
    ) {
        val db = this.writableDatabase


        for (item in staff) {
            val staffId = getStaffId(staff = item, userId = userId)

            val values = ContentValues().apply {

                put("id", staffId)
                put("staffIC", item.id)
                put("staffName", item.name)
                put("salary", item.salary.toString())
                put("userId", userId)
            }
            db.insert("staff", null, values)
        }

        db.close()


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
                val typeName = cursor.getString(typeNameIndex)
                val balance = cursor.getDouble(balanceIndex)

                val cashDetails = Cash(typeName, balance)
                cashDetailsList.add(cashDetails)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return cashDetailsList
    }

    fun addNewCashDetails(
        typeName: String,
        balance: Double,
        userId: String
    ){
        val db = this.writableDatabase
        val values = ContentValues().apply{
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

        // Check if the staff table has any records
        val staffCursor = db.rawQuery("SELECT COUNT(*) FROM staff", null)
        staffCursor.use { cursor ->
            if (cursor.moveToFirst()) {
                val count = cursor.getInt(0)
                isEmpty = isEmpty && (count == 0)
            }
        }
        // Check if the bill table has any records
        val billCursor = db.rawQuery("SELECT COUNT(*) FROM bills", null)
        billCursor.use { cursor ->
            if (cursor.moveToFirst()) {
                val count = cursor.getInt(0)
                isEmpty = isEmpty && (count == 0)
            }
        }
        // Check if the question table has any records
        val questionCursor = db.rawQuery("SELECT COUNT(*) FROM questions", null)
        questionCursor.use { cursor ->
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
        private const val DATABASE_VERSION = 30
    }
}