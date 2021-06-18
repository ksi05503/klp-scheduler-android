package com.example.klp.wholegoal

import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.klp.R
import com.example.klp.customclass.handleSdate
import com.example.klp.data.ScheduleData
import kotlin.math.abs

class GoalFragRecyclerViewAdapter(val scheList:ArrayList<ScheduleData>):RecyclerView.Adapter<GoalFragRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val sname:TextView = view.findViewById(R.id.sname)
        val sdate:TextView = view.findViewById(R.id.sdate)
        val dDay:TextView = view.findViewById(R.id.dDay)
        val stype:TextView = view.findViewById(R.id.stype)
        val percentText:TextView = view.findViewById(R.id.percentText)
        val per_circle:ProgressBar = view.findViewById(R.id.percent_circle)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.goal_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }

        val now = Calendar.getInstance()
//        val start = scheList[position].start
//        val end = scheList[position].end
//        val percent = scheList[position].percent
//        val dDay = (end.timeInMillis - now.timeInMillis)/(24*60*60*1000)
//
//        holder.per_circle.progress = percent
//        holder.percentText.text = scheList[position].percent.toString()+"%"
//        holder.periodText.text = "${start.get(Calendar.YEAR)}.${start.get(Calendar.MONTH)+1}.${start.get(Calendar.DAY_OF_MONTH)}~${end.get(Calendar.YEAR)}.${end.get(Calendar.MONTH)+1}.${end.get(Calendar.DAY_OF_MONTH)}"
//        holder.titleText.text = "${scheList[position].title}"
//        when(scheList[position].category){
//            Category.EXERCISE->{ //운동
//                holder.cateText.text = "[운동]"
//            }
//            Category.STUDY->{ //공부
//                holder.cateText.text = "[공부]"
//            }
//            Category.SCHEDULE->{ //일정
//                holder.cateText.text = "[일정]"
//            }
//        }
//
//        if(dDay > 0){
//            holder.dDayText.text = "D-"+abs(dDay + 1).toString()
//        }
//        else{
//            holder.dDayText.text = "D+"+abs(dDay).toString()
//        }

        now.get(Calendar.YEAR)
        now.get(Calendar.MONTH)
        now.get(Calendar.DATE)

        val hsd = handleSdate(scheList[position].sdate1)
        holder.sname.text = scheList[position].sname
        holder.sdate.text = "${hsd.year}년 ${hsd.month}월 ${hsd.day}일 ${hsd.hour}:${hsd.minute}"
        holder.stype.text = scheList[position].stype

        val dDayValue = calculateDday(hsd.year, hsd.month, hsd.day)
        if(dDayValue > 0){
            holder.dDay.text = "D-"+abs(dDayValue + 1).toString()
        }
        else if(dDayValue==0L){
            holder.dDay.text = "D-DAY"
        }
        else{
            holder.dDay.text = "D+"+abs(dDayValue).toString()
        }
    }

    private fun calculateDday(year:Int, month:Int, day:Int):Long {
        val now = Calendar.getInstance()
        val end = Calendar.getInstance()
        end.set(Calendar.YEAR, year)
        end.set(Calendar.MONTH, month-1)
        end.set(Calendar.DAY_OF_MONTH, day)

        return (end.timeInMillis - now.timeInMillis)/(24*60*60*1000)
    }

    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener

    override fun getItemCount(): Int {
        return scheList.size
    }
}