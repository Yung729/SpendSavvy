package com.example.spendsavvy.data

import com.example.spendsavvy.models.BankAccount

val bankName =  listOf(
    "Affin Bank Berhad",
    "Alliance Bank Malaysia Berhad",
    "AmBank Berhad",
    "Bangkok Bank Berhad",
    "Bank Islam Malaysia Berhad",
    "Bank Muamalat Malaysia Berhad",
    "Bank of America Malaysia Berhad",
    "Bank of China (Malaysia) Berhad",
    "Bank of Tokyo-Mitsubishi (Malaysia) Berhad",
    "Bumiputra-Commerce Bank Berhad",
    "Citibank Berhad",
    "Deutsche Bank (Malaysia) Berhad",
    "EON Bank Berhad",
    "Hong Leong Bank Berhad",
    "HSBC Bank Malaysia Berhad",
    "J.P. Morgan Chase Bank Berhad",
    "J.P. Morgan Chase Bank Berhad",
    "Malayan Banking Berhad",
    "OCBC Bank (Malaysia) Berhad",
    "Public Bank Berhad",
    "RHB Bank Berhad",
    "Southern Bank Berhad",
    "Southern Bank Berhad",
    "Standard Chartered Bank Malaysia Berhad",
    "The Bank of Nova Scotia Berhad",
    "United Overseas Bank (Malaysia) Berhad"
)

fun getBankName() : String{
    return bankName.random()
}

class BankAccountData{
    fun loadBank() : List<BankAccount>{
        return listOf<BankAccount>(
            BankAccount(getBankName(),1000.00),
            BankAccount(getBankName(),1599.00),
            BankAccount(getBankName(),2000.00)
    )
    }
}

