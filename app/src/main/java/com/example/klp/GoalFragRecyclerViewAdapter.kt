package com.example.klp

import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.klp.customclass.handleSdate
import com.example.klp.data.ScheduleData

class GoalFragRecyclerViewAdapter(val scheList:ArrayList<ScheduleData>):RecyclerView.Adapter<GoalFragRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val sname:TextView = view.findViewById(R.id.sname)
        val sdate:TextView = view.findViewById(R.id.sdate)
        val dDay:TextView = view.findViewById(R.id.dDay)
        val stype:TextView = view.findViewById(R.id.stype)
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
        now.get(Calendar.YEAR)
        now.get(Calendar.MONTH)
        now.get(Calendar.DATE)

        val hsd = handleSdate(scheList[position].sdate)
        holder.sname.text = scheList[position].sname

        holder.sdate.text = "${hsd.year}년 ${hsd.month}월 ${hsd.day}일 ${hsd.hour}:${hsd.minute}"
        holder.stype.text = scheList[position].stype
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