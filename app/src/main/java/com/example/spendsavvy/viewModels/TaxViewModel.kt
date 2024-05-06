package com.example.spendsavvy.viewModels

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class TaxViewModel : ViewModel() {
    private val _incomeMonthly = MutableLiveData<Int>()
    var incomeMonthly: LiveData<Int> = _incomeMonthly

    private val _taxMonthly = MutableLiveData<Int>()
    var taxMonthly: LiveData<Int> = _taxMonthly

    private val _incomeAfterTaxMonthly = MutableLiveData<Int>()
    var incomeAfterTaxMonthly: LiveData<Int> = _incomeAfterTaxMonthly

    private val _incomeAnnually = MutableLiveData<Int>()
    var incomeAnnually: LiveData<Int> = _incomeAnnually

    private val _taxAnnually = MutableLiveData<Int>()
    var taxAnnually: LiveData<Int> = _taxAnnually

    private val _incomeAfterTaxAnnually = MutableLiveData<Int>()
    var incomeAfterTaxAnnually: LiveData<Int> = _incomeAfterTaxAnnually
/////////////////////////////////////////////////////////////////////////
    private val _income = MutableLiveData<Int>()
    val income: LiveData<Int> = _income

    private val _tax = MutableLiveData<Int>()
    val tax: LiveData<Int> = _tax

    private val _incomeAfterTax = MutableLiveData<Int>()
    val incomeAfterTax: LiveData<Int> = _incomeAfterTax

    private val _isMonthly = MutableLiveData<Boolean>()
    val isM: LiveData<Boolean> = _isMonthly

    private val _isAnnually = MutableLiveData<Boolean>()
    val isA: LiveData<Boolean> = _isAnnually

    fun calculateTax(initialAmount: String, selectedDate: LocalDate, isMonthly: Boolean, isAnnually: Boolean): Int? {

        val selectedYear = selectedDate.year

        val income: Int = try {
            if (isMonthly) {
                initialAmount.toInt() * 12
            } else {
                initialAmount.toInt()
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

        val tax: Int = when {
            selectedYear == 2023 -> {
                when {
                    income in 0..5000 -> 0
                    income in 5001..20000 -> ((income - 5000) * 0.01).toInt()
                    income in 20001..35000 -> (150 + ((income - 20000) * 0.03)).toInt()
                    income in 35001..50000 -> (600 + ((income - 35000) * 0.06)).toInt()
                    income in 50001..70000 -> (1500 + ((income - 50000)) * 0.11).toInt()
                    income in 70001..100000 -> (3700 + ((income - 70000) * 0.19)).toInt()
                    income in 100001..400000 -> (9400 + ((income - 100000) * 0.25)).toInt()
                    income in 400001..600000 -> (84400 + ((income - 400000) * 0.26)).toInt()
                    income in 600001..2000000 -> (136400 + ((income - 600000) * 0.28)).toInt()
                    income >= 2000001 -> (528400 + ((income - 2000000) * 0.30)).toInt()
                    else -> 0
                }
            }
            selectedYear == 2022 || selectedYear == 2021-> {
                when {
                    income in 0..5000 -> 0
                    income in 5001..20000 -> ((income - 5000) * 0.01).toInt()
                    income in 20001..35000 -> (150 + ((income - 20000) * 0.03)).toInt()
                    income in 35001..50000 -> (600 + ((income - 35000) * 0.08)).toInt()
                    income in 50001..70000 -> (1800 + ((income - 50000)) * 0.13).toInt()
                    income in 70001..100000 -> (4400 + ((income - 70000) * 0.21)).toInt()
                    income in 100001..250000 -> (10700 + ((income - 100000) * 0.24)).toInt()
                    income in 250001..400000 -> (46700 + ((income - 250000) * 0.245)).toInt()
                    income in 400001..600000 -> (83450 + ((income - 400000) * 0.25)).toInt()
                    income in 600001..1000000 -> (133450 + ((income - 600000) * 0.26)).toInt()
                    income in 1000001..2000000 -> (237450 + ((income - 1000000) * 0.28)).toInt()
                    income >= 2000001 -> (517450 + ((income - 2000000) * 0.30)).toInt()
                    else -> 0
                }
            }
            selectedYear == 2020 -> {
                when {
                    income in 0..5000 -> 0
                    income in 5001..20000 -> ((income - 5000) * 0.01).toInt()
                    income in 20001..35000 -> (150 + ((income - 20000) * 0.03)).toInt()
                    income in 35001..50000 -> (600 + ((income - 35000) * 0.08)).toInt()
                    income in 50001..70000 -> (1800 + ((income - 50000)) * 0.13).toInt()
                    income in 70001..100000 -> (4600 + ((income - 70000) * 0.21)).toInt()
                    income in 100001..250000 -> (10900 + ((income - 100000) * 0.24)).toInt()
                    income in 250001..400000 -> (46900 + ((income - 250000) * 0.245)).toInt()
                    income in 400001..600000 -> (83650 + ((income - 400000) * 0.25)).toInt()
                    income in 600001..1000000 -> (133650 + ((income - 600000) * 0.26)).toInt()
                    income in 1000001..2000000 -> (237650 + ((income - 1000000) * 0.28)).toInt()
                    income >= 2000001 -> (517650 + ((income - 2000000) * 0.30)).toInt()
                    else -> 0
                }
            }
            selectedYear == 2019 || selectedYear == 2018 -> {
                when {
                    income in 0..5000 -> 0
                    income in 5001..20000 -> ((income - 5000) * 0.01).toInt()
                    income in 20001..35000 -> (150 + ((income - 20000) * 0.03)).toInt()
                    income in 35001..50000 -> (600 + ((income - 35000) * 0.08)).toInt()
                    income in 50001..70000 -> (1800 + ((income - 50000)) * 0.13).toInt()
                    income in 70001..100000 -> (4600 + ((income - 70000) * 0.21)).toInt()
                    income in 100001..250000 -> (10900 + ((income - 100000) * 0.24)).toInt()
                    income in 250001..400000 -> (46900 + ((income - 250000) * 0.245)).toInt()
                    income in 400001..600000 -> (83650 + ((income - 400000) * 0.25)).toInt()
                    income in 600001..1000000 -> (133650 + ((income - 600000) * 0.26)).toInt()
                    income >= 1000001 -> (237650 + ((income - 1000000) * 0.28)).toInt()
                    else -> 0
                }
            }
            selectedYear == 2017 || selectedYear == 2016 -> {
                when {
                    income in 0..5000 -> 0
                    income in 5001..20000 -> ((income - 5000) * 0.01).toInt()
                    income in 20001..35000 -> (150 + ((income - 20000) * 0.05)).toInt()
                    income in 35001..50000 -> (750 + ((income - 35000) * 0.10)).toInt()
                    income in 50001..70000 -> (1500 + ((income - 50000)) * 0.16).toInt()
                    income in 70001..100000 -> (3200 + ((income - 70000) * 0.21)).toInt()
                    income in 100001..250000 -> (6300 + ((income - 100000) * 0.24)).toInt()
                    income in 250001..400000 -> (11900 + ((income - 250000) * 0.245)).toInt()
                    income in 400001..600000 -> (37900 + ((income - 400000) * 0.25)).toInt()
                    income in 600001..1000000 -> (84650 + ((income - 600000) * 0.26)).toInt()
                    income >= 1000001 -> (138650 + ((income - 1000000) * 0.28)).toInt()
                    else -> 0
                }
            }
            selectedYear == 2015 -> {
                when {
                    income in 0..2500 -> 0
                    income in 2501..5000 -> 0
                    income in 5001..10000 -> ((income - 5000) * 0.01).toInt()
                    income in 10001..20000 -> (50 + ((income - 10000) * 0.01)).toInt()
                    income in 20001..35000 -> (150 + ((income - 20000) * 0.05)).toInt()
                    income in 35001..50000 -> (750 + ((income - 35000) * 0.10)).toInt()
                    income in 50001..70000 -> (3200 + ((income - 50000)) * 0.16).toInt()
                    income in 70001..100000 -> (6300 + ((income - 70000) * 0.21)).toInt()
                    income in 100001..150000 -> (12300 + ((income - 100000) * 0.24)).toInt()
                    income in 150001..250000 -> (24000 + ((income - 150000) * 0.24)).toInt()
                    income in 250001..400000 -> (47900 + ((income - 250000) * 0.245)).toInt()
                    income >= 400001 -> (84650 + ((income - 400000) * 0.25)).toInt()
                    else -> 0
                }
            }
            selectedYear == 2013 || selectedYear == 2014 -> {
                when {
                    income in 0..2500 -> 0
                    income in 2501..5000 -> 0
                    income in 5001..10000 -> ((income - 5000) * 0.02).toInt()
                    income in 10001..20000 -> (100 + ((income - 10000) * 0.02)).toInt()
                    income in 20001..35000 -> (300 + ((income - 20000) * 0.06)).toInt()
                    income in 35001..50000 -> (1200 + ((income - 35000) * 0.11)).toInt()
                    income in 50001..70000 -> (2850 + ((income - 50000)) * 0.19).toInt()
                    income in 70001..100000 -> (6650 + ((income - 70000) * 0.24)).toInt()
                    income >= 100001 -> (13850 + ((income - 100000) * 0.26)).toInt()
                    else -> 0
                }

            }
            else -> 0
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
        println("Income: $income")
        println("Tax: $tax")
        println("Income after Tax: ${_incomeAfterTax.value}")

        return tax
    }
}