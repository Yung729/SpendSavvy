package com.example.spendsavvy.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Calendar
import java.util.Date

class DateSelectionViewModel : ViewModel() {
    var selectedDateFromUser = MutableLiveData<Date>(Calendar.getInstance().apply {
        time = Date()
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time)

    fun setSelectedDate(selectedDate: Date) {
        selectedDateFromUser.value = selectedDate
    }


}