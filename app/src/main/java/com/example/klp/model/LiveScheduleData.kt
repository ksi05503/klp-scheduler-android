package com.example.klp.model

import androidx.lifecycle.MutableLiveData


class LiveScheduleData<T>(private val data: T? = null) : MutableLiveData<T?>() {
    init{
        value = data
    }
    fun setData(data: T?) {
        value = data
    }
}