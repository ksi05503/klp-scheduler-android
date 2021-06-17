package com.example.klp.wholegoal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.klp.adapter.CalendarAdapter
import com.example.klp.adapter.GoalFragStateAdapter
import com.example.klp.databinding.FragmentGoalBinding
import com.google.android.material.tabs.TabLayoutMediator

class GoalFragment : Fragment() {
    var binding:FragmentGoalBinding?=null
    val fragArr = arrayListOf<String>("진행 중인 목표", "종료된 목표")
    var calendarAdapter: CalendarAdapter?=null
    val viewModel: SpinnerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGoalBinding.inflate(layoutInflater, container, false)
        calendarAdapter = CalendarAdapter(requireActivity())
        binding!!.calendar.adapter = calendarAdapter
        binding!!.calendar.setCurrentItem(CalendarAdapter.START_POSITION, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()

    }

    private fun init() {
        binding!!.apply {
            goalViewPager.adapter = GoalFragStateAdapter(requireActivity())
            TabLayoutMediator(binding!!.goalTabLayout, binding!!.goalViewPager){
                    tab, position ->
                tab.text = fragArr[position]
            }.attach()

            goalSpinner.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when(position){
                        0->{
                            Toast.makeText(parent?.context, "사용자 설정 순", Toast.LENGTH_SHORT).show()
                            viewModel.select(0)
                        }
                        1->{
                            Toast.makeText(parent?.context, "남은 기한 순", Toast.LENGTH_SHORT).show()
                            viewModel.select(1)
                        }
                        2->{
                            Toast.makeText(parent?.context, "카테고리 별", Toast.LENGTH_SHORT).show()
                            viewModel.select(2)
                        }
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) { }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null

    }
}