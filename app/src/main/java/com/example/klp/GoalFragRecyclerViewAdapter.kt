package com.example.klp

import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class GoalFragRecyclerViewAdapter(val scheList:List<Schedule>):RecyclerView.Adapter<GoalFragRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val percentText:TextView = view.findViewById(R.id.percentText)
        val periodText:TextView = view.findViewById(R.id.periodText)
        val cateText:TextView = view.findViewById(R.id.cateText)
        val dDayText:TextView = view.findViewById(R.id.dDayText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.goal_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val now = Calendar.getInstance()
        val start = scheList[position].start
        val end = scheList[position].end


        holder.dDayText.text = "D-"+abs((end.timeInMillis - now.timeInMillis) /(24*60*60*1000) + 1).toString()
        holder.percentText.text = scheList[position].percent.toString()+"%"
        holder.periodText.text = "${start.get(Calendar.YEAR)}.${start.get(Calendar.MONTH)+1}.${start.get(Calendar.DAY_OF_MONTH)}~${end.get(Calendar.YEAR)}.${end.get(Calendar.MONTH)+1}.${end.get(Calendar.DAY_OF_MONTH)}"
        holder.cateText.text = "[${scheList[position].category}]${scheList[position].title}"
    }

    override fun getItemCount(): Int {
        return scheList.size
    }
}