package com.example.klp.wholegoal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SpinnerViewModel:ViewModel() {
    val selected = MutableLiveData<Int>()
    fun select(num:Int){
        selected.value = num
    }
}