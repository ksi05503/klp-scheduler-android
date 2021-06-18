package com.example.klp.wholegoal

import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.klp.data.ScheduleData
import com.example.klp.model.ScheduleViewModel
import com.example.klp.databinding.FragmentGoalFirstBinding
import com.example.klp.data.Schedule
import kotlin.collections.ArrayList


class GoalFirstFragment : Fragment() {

    private val scheduleViewModel: ScheduleViewModel by activityViewModels()
    var binding:FragmentGoalFirstBinding?=null
    var recyclerView:RecyclerView?=null
    val viewModel: SpinnerViewModel by activityViewModels()
    var scheduleList = ArrayList<ScheduleData>()
    var adapter: GoalFragRecyclerViewAdapter?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGoalFirstBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.apply {
            recyclerView = binding!!.goalFirstRecycler
            recyclerView!!.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView!!.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )

            scheduleList.addAll(scheduleViewModel.loadAllSchedules())
            adapter = GoalFragRecyclerViewAdapter(scheduleList)
            //클릭이벤트
            adapter!!.setItemClickListener(object :
                GoalFragRecyclerViewAdapter.OnItemClickListener {
                override fun onClick(v: View, position: Int) {
                    val item = adapter!!.scheList[position]

                    Toast.makeText(
                        v.context,
                        "Activity\n${item.sname}\n${item.sdate1}",
                        Toast.LENGTH_SHORT
                    ).show()

                    //다이얼로그
                    adapter!!.notifyDataSetChanged()
                }
            })

            recyclerView!!.adapter = adapter
            adapter!!.scheList.removeIf {
                it.sdone == 1
            }
            adapter!!.notifyDataSetChanged()
/*
        scheduleViewModel.newSchedule.observe(this, Observer {

        })  를 이용해서 값이 변경될때마다 목표 프래그먼트의 리사이클러뷰 아이템을 고쳐주면 될듯 하다.
*/

            viewModel.selected.observe(viewLifecycleOwner, Observer {
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
    }

    private fun generateCal(year:Int, month:Int, day:Int):Calendar{
        var cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month-1)
        cal.set(Calendar.DAY_OF_MONTH, day)
        return cal
    }


}