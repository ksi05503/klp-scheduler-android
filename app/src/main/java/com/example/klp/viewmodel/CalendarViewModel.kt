package com.example.klp.viewmodel

import android.icu.util.Calendar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalendarViewModel:ViewModel() {
    val mCalendar = MutableLiveData<Calendar>()
    fun setCalendar(calendar: Calendar){
        this.mCalendar.value = calendar
    }
}