package com.example.spendsavvy.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Calendar
import java.util.Date

class DateSelectionViewModel : ViewModel() {
    var selectedDateFromUser = MutableLiveData<Date>()

    fun setSelectedDate(selectedDate: Date) {
        selectedDateFromUser.value = selectedDate
    }


}