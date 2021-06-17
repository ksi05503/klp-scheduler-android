package com.example.klp

import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.klp.data.ScheduleData
import com.example.klp.data.ScheduleViewModel
import com.example.klp.databinding.FragmentGoalFirstBinding
import java.lang.Exception
import kotlin.collections.ArrayList


class GoalFirstFragment : Fragment() {

    private val scheduleViewModel: ScheduleViewModel by activityViewModels()


    var binding:FragmentGoalFirstBinding?=null
    var recyclerView:RecyclerView?=null
    var adapter:GoalFragRecyclerViewAdapter?=null
    var scheduleList = mutableListOf<Schedule>()
    var scheduleDataList = ArrayList<ScheduleData>()
    var mCalendarList:MutableLiveData<ArrayList<Object>>?=null
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
        // scheduleViewModel.loadAllSchedules() 이걸 어댑터에서 사용
        scheduleList.add(Schedule(0, "운동", "러닝", cal1, cal2))

        binding.apply {
            recyclerView = binding!!.goalFirstRecycler

            scheduleDataList = scheduleViewModel.loadAllSchedules()
            adapter = GoalFragRecyclerViewAdapter(scheduleDataList)
            //클릭이벤트
            adapter!!.setItemClickListener(object : GoalFragRecyclerViewAdapter.OnItemClickListener{
                override fun onClick(v: View, position: Int) {
                    val item = scheduleDataList[position]

                    Toast.makeText(v.context, "Activity\n${item.sname}\n${item.sdate}", Toast.LENGTH_SHORT).show()

                    //다이얼로그








                    adapter!!.notifyDataSetChanged()
                }
            })


            recyclerView!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView!!.adapter = adapter



        }
/*
        scheduleViewModel.newSchedule.observe(this, Observer {

        })  를 이용해서 값이 변경될때마다 목표 프래그먼트의 리사이클러뷰 아이템을 고쳐주면 될듯 하다.
*/

    }

    private fun setCalendarList(){
        var cal = GregorianCalendar()
        var calendarList = ArrayList<Object>()

        for(i:Int in -300..300){
            try{
                var calendar = GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + i, 1, 0, 0, 0)
                calendarList.add(calendar.timeInMillis as Object)

                val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1 //해당 월  시작 요일 - 1 = 빈칸
                val max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) //해당 월 마지막 요일
                for(j in 0 until dayOfWeek){
                    calendarList.add("empty" as Object)
                }
                for(j in 1..max){
                    calendarList.add(GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), j) as Object)
                }
            }
            catch(e:Exception){
                Log.e("Exception", e.toString())
            }
        }
        mCalendarList!!.value = calendarList
    }


}