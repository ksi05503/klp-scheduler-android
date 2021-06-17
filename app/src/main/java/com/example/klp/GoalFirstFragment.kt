package com.example.klp

import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.klp.data.ScheduleViewModel
import com.example.klp.adapter.GoalFragRecyclerViewAdapter
import com.example.klp.databinding.FragmentGoalFirstBinding
import com.example.klp.datas.Schedule
import com.example.klp.model.GoalViewModel
import com.example.klp.utils.Category
import java.lang.Exception
import kotlin.collections.ArrayList


class GoalFirstFragment : Fragment() {

    private val scheduleViewModel: ScheduleViewModel by activityViewModels()

    var binding:FragmentGoalFirstBinding?=null
    var recyclerView:RecyclerView?=null
    var adapter: GoalFragRecyclerViewAdapter?=null
    val viewModel:GoalViewModel by activityViewModels()
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
            adapter = GoalFragRecyclerViewAdapter(ArrayList<Schedule>())

            recyclerView!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView!!.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
            recyclerView!!.adapter = adapter

        }
/*
        scheduleViewModel.newSchedule.observe(this, Observer {

        })  를 이용해서 값이 변경될때마다 목표 프래그먼트의 리사이클러뷰 아이템을 고쳐주면 될듯 하다.
*/

            //테스트 코드
            var cal1 = generateCal(2021, 5, 1)
            var cal2 = generateCal(2021, 5, 5)
            var cal3 = generateCal(2021, 5, 15)
            var cal4 = generateCal(2021, 6, 20)
            var cal5 = generateCal(2021, 6, 30)

            adapter!!.scheList.add(Schedule(15, Category.STUDY, "토익", cal3, cal5))
            adapter!!.scheList.add(Schedule(60, Category.EXERCISE, "러닝", cal2, cal3))
            adapter!!.scheList.add(Schedule(90, Category.EXERCISE, "턱걸이", cal1, cal2))
            adapter!!.scheList.add(Schedule(38, Category.SCHEDULE, "면접 준비", cal3, cal4))
            adapter!!.notifyDataSetChanged()


        viewModel.selected.observe(viewLifecycleOwner, Observer {
            when(it){
                0->{

                }
                1->{
                    adapter!!.scheList.sortBy { it.end }
                    adapter!!.notifyDataSetChanged()
                }
                2->{
                    adapter!!.scheList.sortBy { it.category }
                    adapter!!.notifyDataSetChanged()
                }
            }
        })
    }

    private fun generateCal(year:Int, month:Int, day:Int):Calendar{
        var cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month-1)
        cal.set(Calendar.DAY_OF_MONTH, day)
        return cal
    }


}