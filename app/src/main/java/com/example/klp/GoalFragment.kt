package com.example.klp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.klp.databinding.FragmentGoalBinding
import com.google.android.material.tabs.TabLayoutMediator

class GoalFragment : Fragment() {
    var binding:FragmentGoalBinding?=null
    val fragArr = arrayListOf<String>("진행 중인 목표", "종료된 목표")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGoalBinding.inflate(layoutInflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
    }

    private fun init() {
        binding!!.goalViewPager.adapter = GoalFragStateAdapter(this.requireActivity())
        TabLayoutMediator(binding!!.goalTabLayout, binding!!.goalViewPager){
            tab, position ->
            tab.text = fragArr[position]
        }.attach()
    }

}