package com.example.klp.wholegoal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.klp.databinding.FragmentCalendarBinding
import com.example.klp.utils.CalendarUtils
import org.joda.time.DateTime

class CalendarFragment : Fragment() {
    companion object{
        private const val MILLIS = "MILLIS"
        var binding:FragmentCalendarBinding?=null
        fun newInstance(millis:Long) = CalendarFragment().apply {
            arguments = Bundle().apply {
                putLong(MILLIS, millis)
            }
        }
    }
    private var millis:Long = 0L
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
        binding!!.millis.text = DateTime(millis).toString("yyyy년 MM월")
        binding!!.calendarView.initCalendar(DateTime(millis), CalendarUtils.getMonthList(DateTime(millis)))
        return binding!!.root
    }

}