package com.example.spendsavvy.data

import com.example.spendsavvy.State.BankAccount

val bankName =  listOf(
    "Affin Bank Berhad",
    "Alliance Bank Malaysia Berhad",
    "AmBank Berhad",
    "Bumiputra-Commerce Bank Berhad",
    "EON Bank Berhad",
    "Hong Leong Bank Berhad",
    "Malayan Banking Berhad",
    "Public Bank Berhad",
    "RHB Bank Berhad",
    "Southern Bank Berhad",
    "Southern Bank Berhad",
    "Bangkok Bank Berhad",
    "Bank of America Malaysia Berhad",
    "Bank of China (Malaysia) Berhad",
    "Bank of Tokyo-Mitsubishi (Malaysia) Berhad",
    "Citibank Berhad",
    "Deutsche Bank (Malaysia) Berhad",
    "HSBC Bank Malaysia Berhad",
    "J.P. Morgan Chase Bank Berhad",
    "J.P. Morgan Chase Bank Berhad",
    "OCBC Bank (Malaysia) Berhad",
    "Standard Chartered Bank Malaysia Berhad",
    "The Bank of Nova Scotia Berhad",
    "United Overseas Bank (Malaysia) Berhad",
    "Bank Islam Malaysia Berhad",
    "Bank Muamalat Malaysia Berhad"
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

