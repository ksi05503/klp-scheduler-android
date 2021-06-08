package com.example.klp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.klp.CommunityFragment
import com.example.klp.GoalFragment
import com.example.klp.StatisticsFragment
import com.example.klp.TodayFragment

class MyFragStateAdapter(fragmentActivity:FragmentActivity):FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> GoalFragment()
            1-> TodayFragment()
            2-> StatisticsFragment()
            3-> CommunityFragment()
            else-> GoalFragment()
        }
    }
}