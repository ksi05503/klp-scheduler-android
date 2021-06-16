package com.example.klp.adapter

import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.klp.R
import com.example.klp.datas.Schedule
import kotlin.math.abs

class GoalFragRecyclerViewAdapter(val scheList:List<Schedule>):RecyclerView.Adapter<GoalFragRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val percentText:TextView = view.findViewById(R.id.percentText)
        val periodText:TextView = view.findViewById(R.id.periodText)
        val cateText:TextView = view.findViewById(R.id.cateText)
        val dDayText:TextView = view.findViewById(R.id.dDayText)
        val per_circle:ProgressBar = view.findViewById(R.id.percent_circle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.goal_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val now = Calendar.getInstance()
        val start = scheList[position].start
        val end = scheList[position].end
        val percent = scheList[position].percent
        val dDay = (end.timeInMillis - now.timeInMillis) /(24*60*60*1000) + 1

        holder.per_circle.progress = percent
        holder.percentText.text = scheList[position].percent.toString()+"%"
        holder.periodText.text = "${start.get(Calendar.YEAR)}.${start.get(Calendar.MONTH)+1}.${start.get(Calendar.DAY_OF_MONTH)}~${end.get(Calendar.YEAR)}.${end.get(Calendar.MONTH)+1}.${end.get(Calendar.DAY_OF_MONTH)}"
        holder.cateText.text = "[${scheList[position].category}]${scheList[position].title}"

        if(dDay > 0){
            holder.dDayText.text = "D-"+abs(dDay).toString()
        }
        else{
            holder.dDayText.text = "D+"+abs(dDay).toString()
        }

    }

    override fun getItemCount(): Int {
        return scheList.size
    }
}