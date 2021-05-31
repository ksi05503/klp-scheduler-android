package com.example.klp.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelForStatsTab : ViewModel() {
    private val _selectedTab = MutableLiveData<Int>()
    val selectedTab: LiveData<Int>
        get() = _selectedTab

    fun setTab(tab: Int) {
        _selectedTab.value = tab
    }
}
