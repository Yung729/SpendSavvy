package com.example.spendsavvy.viewModels

import androidx.lifecycle.ViewModel
import java.time.LocalDate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class TaxViewModel : ViewModel() {
    private val _incomeMonthly = MutableLiveData<Double>()
    var incomeMonthly: LiveData<Double> = _incomeMonthly

    private val _taxMonthly = MutableLiveData<Double>()
    var taxMonthly: LiveData<Double> = _taxMonthly

    private val _incomeAfterTaxMonthly = MutableLiveData<Double>()
    var incomeAfterTaxMonthly: LiveData<Double> = _incomeAfterTaxMonthly

    private val _incomeAnnually = MutableLiveData<Double>()
    var incomeAnnually: LiveData<Double> = _incomeAnnually

    private val _taxAnnually = MutableLiveData<Double>()
    var taxAnnually: LiveData<Double> = _taxAnnually

    private val _incomeAfterTaxAnnually = MutableLiveData<Double>()
    var incomeAfterTaxAnnually: LiveData<Double> = _incomeAfterTaxAnnually

    private val _income = MutableLiveData<Double>()
    private val _tax = MutableLiveData<Double>()
    private val _incomeAfterTax = MutableLiveData<Double>()
    private val _isMonthly = MutableLiveData<Boolean>()
    private val _isAnnually = MutableLiveData<Boolean>()

    fun calculateTax(initialAmount: String, selectedDate: LocalDate, isMonthly: Boolean, isAnnually: Boolean): Double? {

        val selectedYear = selectedDate.year

        val income: Double = try {
            if (isMonthly) {
                initialAmount.toDouble() * 12
            } else {
                initialAmount.toDouble()
            }
        } catch (e: NumberFormatException) {
            println("Invalid initial amount: $initialAmount")
            return null
        }

        _isMonthly.value = isMonthly
        _isAnnually.value = isAnnually

        _income.value = income
        _incomeMonthly.value = income /12
        _incomeAnnually.value = income

        val tax: Double = when (selectedYear) {
            2023 -> {
                when {
                    income in 0.0..5000.0 -> 0.0
                    income in 5001.0..20000.0 -> ((income - 5000) * 0.01)
                    income in 20001.0..35000.0 -> (150 + ((income - 20000) * 0.03))
                    income in 35001.0..50000.0 -> (600 + ((income - 35000) * 0.06))
                    income in 50001.0..70000.0 -> (1500 + ((income - 50000)) * 0.11)
                    income in 70001.0..100000.0 -> (3700 + ((income - 70000) * 0.19))
                    income in 100001.0..400000.0 -> (9400 + ((income - 100000) * 0.25))
                    income in 400001.0..600000.0 -> (84400 + ((income - 400000) * 0.26))
                    income in 600001.0..2000000.0 -> (136400 + ((income - 600000) * 0.28))
                    income >= 2000001.0 -> (528400 + ((income - 2000000) * 0.30))
                    else -> 0.0
                }
            }
            2022, 2021 -> {
                when {
                    income in 0.0..5000.0 -> 0.0
                    income in 5001.0..20000.0 -> ((income - 5000) * 0.01)
                    income in 20001.0..35000.0 -> (150 + ((income - 20000) * 0.03))
                    income in 35001.0..50000.0 -> (600 + ((income - 35000) * 0.08))
                    income in 50001.0..70000.0 -> (1800 + ((income - 50000)) * 0.13)
                    income in 70001.0..100000.0 -> (4400 + ((income - 70000) * 0.21))
                    income in 100001.0..250000.0 -> (10700 + ((income - 100000) * 0.24))
                    income in 250001.0..400000.0 -> (46700 + ((income - 250000) * 0.245))
                    income in 400001.0..600000.0 -> (83450 + ((income - 400000) * 0.25))
                    income in 600001.0..1000000.0 -> (133450 + ((income - 600000) * 0.26))
                    income in 1000001.0..2000000.0 -> (237450 + ((income - 1000000) * 0.28))
                    income >= 2000001.0 -> (517450 + ((income - 2000000) * 0.30))
                    else -> 0.0
                }
            }
            else -> 0.0
        }
        _tax.value = tax
        _taxMonthly.value = tax /12
        _taxAnnually.value = tax

        _incomeAfterTax.value = income - tax
        _incomeAfterTaxMonthly.value = (income - tax)/12
        _incomeAfterTaxAnnually.value = income - tax

        println("Initial Amount: $initialAmount")
        println("Selected Year: $selectedYear")
        println("Is Monthly: $isMonthly")
        println("Is Annually: $isAnnually")
        println("Income(M): ${_incomeMonthly.value}")
        println("Tax(M): ${_taxMonthly.value}")
        println("Income after Tax(M): ${_incomeAfterTaxMonthly.value}")

        println("Income(A): ${_incomeAnnually.value}")
        println("Tax(A): ${_taxAnnually.value}")
        println("Income after Tax(A): ${_incomeAfterTaxAnnually.value}")

        return tax
    }
}
