package com.example.spendsavvy.data

import android.net.Uri
import com.example.spendsavvy.models.Category

class CategoryData {

    fun loadCategory(mode: Int = 2): List<Category> {
        val filterCatData = mutableListOf<Category>()

        val categoryData = listOf(
            Category(
                id = "T0001",
                imageUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/spendsavvy-5a2a8.appspot.com/o/images%2Felectrical.png?alt=media&token=1c704487-eda9-4135-b27a-aa0ee7ff3762"),
                categoryName = "Electrical",
                categoryType = "Expenses"
            ),
            Category(
                id = "T0002",
                imageUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/spendsavvy-5a2a8.appspot.com/o/images%2Fstock.png?alt=media&token=416dc2e0-caf2-4c9e-a664-2c0eceba49fb"),
                categoryName = "Stock Sales",
                categoryType = "Incomes"
            ),
            Category(
                id = "T0003",
                imageUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/spendsavvy-5a2a8.appspot.com/o/images%2Fconsult.png?alt=media&token=1dd1ffda-f6e1-41b9-a68a-721a55f0578e"),
                categoryName = "Consultation Fee",
                categoryType = "Incomes"
            ),
            Category(
                id = "T0004",
                imageUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/spendsavvy-5a2a8.appspot.com/o/images%2Fdonation.png?alt=media&token=54c95345-4c9f-4982-b777-a83ddacb0897"),
                categoryName = "Donation",
                categoryType = "Incomes"
            ),
            Category(
                id = "T0005",
                imageUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/spendsavvy-5a2a8.appspot.com/o/images%2Finsurans.png?alt=media&token=92f5722d-b84f-4694-868c-b35db0417b7c"),
                categoryName = "Insurance",
                categoryType = "Expenses"
            ),
            Category(
                id = "T0006",
                imageUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/spendsavvy-5a2a8.appspot.com/o/images%2Foffice-supplies.png?alt=media&token=cbe9f5c1-4b91-4b90-a6d2-0683ea7a4602"),
                categoryName = "Office Equipment",
                categoryType = "Expenses"
            ),
            Category(
                id = "T0007",
                imageUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/spendsavvy-5a2a8.appspot.com/o/images%2Fsubscription.png?alt=media&token=0eebcc63-8d05-4206-93ac-40111bdba0e7"),
                categoryName = "Subscription",
                categoryType = "Incomes"
            ),
            Category(
                id = "T0008",
                imageUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/spendsavvy-5a2a8.appspot.com/o/images%2Ftransportation.png?alt=media&token=2eaad609-14be-435c-893f-ceda19737be1"),
                categoryName = "Transportation",
                categoryType = "Expenses"
            ),
            Category(
                id = "T0009",
                imageUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/spendsavvy-5a2a8.appspot.com/o/images%2FincomeTax.png?alt=media&token=c4d11810-731f-41e0-a248-a921733754d2"),
                categoryName = "Income Tax",
                categoryType = "Expenses"
            ),
            Category(
                id = "T0010",
                imageUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/spendsavvy-5a2a8.appspot.com/o/images%2Finstallment.png?alt=media&token=7300217d-6385-4519-aa31-be3a093e0fa5"),
                categoryName = "Installment",
                categoryType = "Expenses"
            ),
            Category(
                id = "T0011",
                imageUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/spendsavvy-5a2a8.appspot.com/o/images%2Fbill.png?alt=media&token=46eddfac-b820-490f-8a4b-5fe2b6f3404f"),
                categoryName = "Bill",
                categoryType = "Expenses"
            ),
            Category(
                id = "T0012",
                imageUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/spendsavvy-5a2a8.appspot.com/o/images%2FUtilities.png?alt=media&token=2a06fa9b-1643-4499-8efc-0d3984cf84f4"),
                categoryName = "Utilities",
                categoryType = "Expenses"
            ),
            Category(
                id = "T0013",
                imageUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/spendsavvy-5a2a8.appspot.com/o/images%2Fsalary.png?alt=media&token=102737bc-9da6-48ef-827c-b0b05d9fb052"),
                categoryName = "Staff Salary",
                categoryType = "Expenses"
            )

        )

        when (mode) {
            0 -> {
                for (category in categoryData) {
                    if (category.categoryType == "Expenses") {
                        filterCatData.add(category)
                    }
                }
            }

            1 -> {
                for (category in categoryData) {
                    if (category.categoryType == "Incomes") {
                        filterCatData.add(category)
                    }
                }
            }

            2 -> {
                return categoryData
            }
        }

        return filterCatData

    }
}