package com.example.klp.wholegoal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.klp.data.ScheduleData
import com.example.klp.databinding.FragmentGoalSecondBinding
import com.example.klp.model.ScheduleViewModel

class GoalSecondFragment : Fragment() {
    var binding:FragmentGoalSecondBinding?=null
    var adapter:GoalFragRecyclerViewAdapter?=null
    var scheduleList = ArrayList<ScheduleData>()
    val scheduleViewModel:ScheduleViewModel by activityViewModels()
    val spinViewModel: SpinnerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentGoalSecondBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))

            scheduleList.addAll(scheduleViewModel.loadAllSchedules())
            adapter = GoalFragRecyclerViewAdapter(scheduleList)
            recyclerView.adapter = adapter
        }

        adapter!!.scheList.removeIf {
            it.sdone == 0
        }

        spinViewModel.selected.observe(viewLifecycleOwner, Observer {
            when (it) {
                0 -> {
                    adapter!!.scheList.sortBy { it.simportance}
                    adapter!!.scheList.reverse()
                    adapter!!.notifyDataSetChanged()
                }
                1 -> {
                    adapter!!.scheList.sortBy { it.sdate2}
                    adapter!!.notifyDataSetChanged()
                }
                2 -> {
                    adapter!!.scheList.sortBy { it.stype}
                    adapter!!.notifyDataSetChanged()
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}