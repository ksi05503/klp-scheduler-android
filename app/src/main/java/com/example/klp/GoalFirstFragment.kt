package com.example.klp

import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.klp.databinding.FragmentGoalFirstBinding
import java.util.*


class GoalFirstFragment : Fragment() {
    var binding:FragmentGoalFirstBinding?=null
    var recyclerView:RecyclerView?=null
    var adapter:GoalFragRecyclerViewAdapter?=null
    var scheduleList = mutableListOf<Schedule>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGoalFirstBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var cal1 = Calendar.getInstance()
        cal1.set(Calendar.YEAR, 2021)
        cal1.set(Calendar.MONTH, Calendar.MAY)
        cal1.set(Calendar.DAY_OF_MONTH, 5)
        var cal2 = Calendar.getInstance()
        cal2.set(Calendar.YEAR, 2021)
        cal2.set(Calendar.MONTH, Calendar.MAY)
        cal2.set(Calendar.DAY_OF_MONTH, 31)
        scheduleList.add(Schedule(0, "운동", "러닝", cal1, cal2))

        binding.apply {
            recyclerView = binding!!.goalFirstRecycler
            adapter = GoalFragRecyclerViewAdapter(scheduleList)

            recyclerView!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView!!.adapter = adapter
        }
    }
}