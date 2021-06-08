package com.example.klp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.klp.GoalFirstFragment
import com.example.klp.GoalSecondFragment

class GoalFragStateAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> GoalFirstFragment()
            1-> GoalSecondFragment()
            else-> GoalFirstFragment()
        }
    }
}