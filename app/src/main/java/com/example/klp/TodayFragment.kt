package com.example.klp

import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.klp.adapter.GoalFragRecyclerViewAdapter
import com.example.klp.data.ScheduleData
import com.example.klp.databinding.FragmentTodayBinding


class TodayFragment : Fragment() {
    var binding:FragmentTodayBinding?=null
    var adapter:GoalFragRecyclerViewAdapter?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodayBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerView!!.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
            adapter = GoalFragRecyclerViewAdapter(ArrayList<ScheduleData>())
            recyclerView.adapter = adapter
            adapter!!.apply {
                var cal1 = Calendar.getInstance()
                cal1.set(Calendar.YEAR, 2021)
                cal1.set(Calendar.MONTH, Calendar.MAY)
                cal1.set(Calendar.DAY_OF_MONTH, 1)
                var cal2 = Calendar.getInstance()
                cal2.set(Calendar.YEAR, 2021)
                cal2.set(Calendar.MONTH, Calendar.JUNE)
                cal2.set(Calendar.DAY_OF_MONTH, 30)


//                scheList.add(Schedule(35, Category.STUDY, "토익 문제집", cal1, cal2))
            }
        }
    }
}