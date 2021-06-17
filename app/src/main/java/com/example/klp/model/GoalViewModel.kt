package com.example.klp.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GoalViewModel:ViewModel() {
    val selected = MutableLiveData<Int>()
    fun select(num:Int){
        selected.value = num
    }
}