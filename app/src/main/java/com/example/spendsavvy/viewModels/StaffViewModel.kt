package com.example.spendsavvy.viewModels

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendsavvy.db.DatabaseHelper
import com.example.spendsavvy.models.Category
import com.example.spendsavvy.models.Staff
import com.example.spendsavvy.models.Transactions
import com.example.spendsavvy.repo.FirestoreRepository
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class StaffViewModel(
    context: Context, isOnline: Boolean, userId: String, transactionViewModel: OverviewViewModel
) : ViewModel() {


    private val firestoreRepository = FirestoreRepository()
    private val dbHelper = DatabaseHelper(context)
    private val transactionViewModel = transactionViewModel

    @SuppressLint("StaticFieldLeak")
    val currentContext = context
    private val internet = isOnline
    private val currentUserId = userId


    val staffList = MutableLiveData<List<Staff>>()
    val totalStaffCount = MutableLiveData<Int>()
    val totalStaffSalary = MutableLiveData<Double>()


    init {
        getStaffList()
    }


    fun getStaffList(userId: String = currentUserId) {
        viewModelScope.launch {
            try {
                val staffFromDB: List<Staff> = if (internet) {
                    firestoreRepository.readItemsFromDatabase(
                        userId, "Staff", Staff::class.java
                    )
                } else {
                    dbHelper.readStaff(userId)
                }


                updateStaff(staffFromDB)


            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error getting staffs", e)
            }
        }
    }

    private fun updateStaff(staff: List<Staff>) {
        viewModelScope.launch {
            var count = 0
            var salaryTotal = 0.0
            staff.forEach {
                count++
                salaryTotal += it.salary
            }

            staffList.postValue(staff)
            totalStaffCount.postValue(count)
            totalStaffSalary.postValue(salaryTotal)
        }
    }


    private suspend fun getStaffId(staff: Staff): String {
        return firestoreRepository.getDocumentId("Staff", currentUserId, staff)
    }

    fun editStaff(staff: Staff, updatedStaff: Staff) {
        viewModelScope.launch {
            try {
                val staffId: String = getStaffId(staff)


                firestoreRepository.updateItemInFirestoreById(currentUserId,
                    "Staff",
                    staffId,
                    updatedStaff,
                    onSuccess = {

                        dbHelper.updateStaff(
                            staffId,
                            updatedStaff.id,
                            updatedStaff.name,
                            updatedStaff.salary,
                            currentUserId
                        )

                        val currentStaffList = staffList.value ?: emptyList()
                        val updatedStaffList = currentStaffList.map {
                            if (it == staff) updatedStaff else it
                        }
                        updateStaff(staff = updatedStaffList)


                        Toast.makeText(
                            currentContext, "Staff edited", Toast.LENGTH_SHORT
                        ).show()
                    })

            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error editing staff", e)
            }
        }
    }

    fun deleteStaff(staff: Staff) {
        viewModelScope.launch {
            try {
                val staffId: String = getStaffId(staff)



                firestoreRepository.deleteItemFromFirestoreById(currentUserId,
                    "Staff",
                    staffId,
                    onSuccess = {

                        dbHelper.deleteStaff(staffId, currentUserId)

                        val currentStaffList = staffList.value ?: emptyList()
                        val updatedStaffList = currentStaffList.filter { it != staff }
                        updateStaff(staff = updatedStaffList)

                        Toast.makeText(
                            currentContext, "Staff deleted", Toast.LENGTH_SHORT
                        ).show()
                    })


            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error deleting staff", e)
            }
        }
    }


    fun addStaffToDatabase(staff: Staff) {

        if (staffList.value?.any { it.id == staff.id } == true) {
            Toast.makeText(
                currentContext, "Category with the same name already exists", Toast.LENGTH_SHORT
            ).show()
            return
        }

        firestoreRepository.addItem(currentUserId, "Staff", staff, "S%04d",
            onSuccess = { documentId ->

                dbHelper.addNewStaff(documentId, staff.id, staff.name, staff.salary, currentUserId)

                val currentStaffList = staffList.value ?: emptyList()
                val updatedStaffList = currentStaffList + staff
                updateStaff(staff = updatedStaffList)

                Toast.makeText(
                    currentContext, "Staff added", Toast.LENGTH_SHORT
                ).show()

            }, onFailure = {


            })
    }

    fun addTotalStaffSalaryToExpenses() {
        viewModelScope.launch {
            val totalSalary = totalStaffSalary.value ?: 0.0

            // Add the expense to Firestore or wherever you store your expenses
            transactionViewModel.addTransactionToFirestore(
                Transactions(
                    id = transactionViewModel.generateTransactionId(),
                    amount = totalSalary,
                    description = "Salary",
                    date = Date(),
                    category = Category(
                        id = "T0013",
                        imageUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/spendsavvy-5a2a8.appspot.com/o/images%2Fsalary.png?alt=media&token=102737bc-9da6-48ef-827c-b0b05d9fb052"),
                        categoryName = "Staff Salary",
                        categoryType = "Expenses"
                    ),
                    transactionType = "Expenses"
                ),
                onSuccess = {
                    Toast.makeText(
                        currentContext,
                        "Total staff salary added to expenses successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onFailure = {
                    Toast.makeText(
                        currentContext,
                        "Failed to add total staff salary to expenses",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }



}