package com.example.klp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.klp.community.CommunityFragment
import com.example.klp.wholegoal.GoalFragment
import com.example.klp.todaygoal.TodayFragment
import com.example.klp.statistics.StatisticsFragment

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