package com.example.klp.todaygoal

import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.klp.wholegoal.GoalFragRecyclerViewAdapter
import com.example.klp.data.ScheduleData
import com.example.klp.databinding.FragmentTodayBinding
import com.example.klp.model.ScheduleViewModel
import com.example.klp.wholegoal.SpinnerViewModel


class TodayFragment : Fragment() {
    var binding:FragmentTodayBinding?=null
    var adapter: GoalFragRecyclerViewAdapter?=null
    val scheduleViewModel:ScheduleViewModel by activityViewModels()
    val spinnerViewModel:SpinnerViewModel by activityViewModels()
    var scheduleList=ArrayList<ScheduleData>()

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

            scheduleList.addAll(scheduleViewModel.loadAllSchedules())
            adapter = GoalFragRecyclerViewAdapter(scheduleList)
            recyclerView.adapter = adapter
        }

        todayFiltering()
    }

    private fun todayFiltering() {
        val now = Calendar.getInstance()
        val year = now.get(Calendar.YEAR)
        val month = now.get(Calendar.MONTH)+1
        val date = now.get(Calendar.DAY_OF_MONTH)
        val dow = now.get(Calendar.DAY_OF_WEEK)

        scheduleList.removeIf {
            val ymdStr = it.sdate2.split("-")
            val tarYear = ymdStr[0].toInt()
            val tarMonth = ymdStr[1].toInt()
            val tarDate = ymdStr[2].toInt()

            //아래 조건이면 삭제
            it.sdone==1 // 이미 완료된 일정
                    || (it.sregular==0 && (year!=tarYear || month!=tarMonth || date!=tarDate)) //마감일이 오늘이 아닐 때
                    || ((it.sregular==2 || it.sregular==3) && (it.sweekly != dow)) // 매주, 매달인데 요일이 동일
        }
    }
}