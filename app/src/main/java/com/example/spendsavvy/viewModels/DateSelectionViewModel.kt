package com.example.spendsavvy.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Calendar
import java.util.Date

class DateSelectionViewModel : ViewModel() {
    var selectedDateFromUser = MutableLiveData<Date>()
    var selectedStartDate = MutableLiveData<Date>()
    var selectedEndDate = MutableLiveData<Date>()

    fun setSelectedDate(selectedDate: Date) {
        selectedDateFromUser.value = selectedDate
    }

    fun setStartDate(selectedDate: Date) {
        selectedStartDate.value = selectedDate
    }
    fun setEndDate(selectedDate: Date) {
        selectedEndDate.value = selectedDate
    }


}