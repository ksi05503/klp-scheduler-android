package com.example.klp.wholegoal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.klp.customview.CalendarView
import com.example.klp.data.ScheduleData
import com.example.klp.databinding.FragmentCalendarBinding
import com.example.klp.model.ScheduleViewModel
import com.example.klp.utils.CalendarUtils
import org.joda.time.DateTime

class CalendarFragment : Fragment() {
    var binding:FragmentCalendarBinding?=null
    var calendarView:CalendarView?=null
    val scheduleViewModel:ScheduleViewModel by activityViewModels()
    var scheduleList = ArrayList<ScheduleData>()

    companion object{
        private const val MILLIS = "MILLIS"
        fun newInstance(millis:Long) = CalendarFragment().apply {
            arguments = Bundle().apply {
                putLong(MILLIS, millis)
            }
        }
    }
    var millis:Long = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            millis = it.getLong(MILLIS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scheduleList = scheduleViewModel.newSchedules.value!!

        binding!!.millis.text = DateTime(millis).toString("yyyy년 MM월")
        calendarView = binding!!.calendarView
        binding!!.calendarView.initCalendar(DateTime(millis), CalendarUtils.getMonthList(DateTime(millis)), scheduleList)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}