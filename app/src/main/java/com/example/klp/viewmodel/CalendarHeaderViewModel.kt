package com.example.klp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalendarHeaderViewModel: ViewModel() {
    val mHeaderDate = MutableLiveData<Long>()
    fun setHeaderData(headerDate:Long){
        this.mHeaderDate.value = headerDate
    }
}