package com.example.klp.wholegoal

import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.klp.customclass.handleSdate
import com.example.klp.data.ScheduleData
import com.example.klp.databinding.GoalRowBinding
import kotlin.math.abs


class GoalFragRecyclerViewAdapter(var scheList:ArrayList<ScheduleData>?):RecyclerView.Adapter<GoalFragRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(binding: GoalRowBinding):RecyclerView.ViewHolder(binding.root){
        val sname: TextView = binding.sname
        val sdate: TextView = binding.sdate
        val sregular:TextView = binding.sregular
        val dDay: TextView = binding.dDay
        val stype: TextView = binding.stype
        val percentText: TextView = binding.percentText
        val per_circle: ProgressBar = binding.percentCircle
    }

    fun setData(data: ArrayList<ScheduleData>?) {
        scheList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            GoalRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }

        val hsd = handleSdate(scheList!![position].SDATE1)
        holder.sname.text = scheList!![position].SNAME
        holder.sdate.text = "${hsd.year}년 ${hsd.month}월 ${hsd.day}일"
        holder.stype.text = scheList!![position].STYPE

        //주기 표시
        when(scheList!![position].SREGULAR){
            0->holder.sregular.text = ""
            1->holder.sregular.text = "매일"
            2->holder.sregular.text = "매주"
            3->holder.sregular.text = "매달"
        }

        //DDAY 계산
        val ymdStr = scheList!![position].SDATE2.split("-")
        val year = ymdStr[0].toInt()
        val month = ymdStr[1].toInt()
        val day = (ymdStr[2].subSequence(0, 2) as String).toInt()

        val dDayValue = calculateDday(year, month, day)
        if(dDayValue > 0){
            holder.dDay.text = "D-"+abs(dDayValue).toString()
        }
        else if(dDayValue==0L){
            holder.dDay.text = "D-DAY"
        } else {
            holder.dDay.text = "D+" + abs(dDayValue).toString()
        }

        //퍼센트 계산 (경과율)
        val start = scheList!![position].SDATE1.split("-")
        val end = scheList!![position].SDATE2.split("-")
        val now = Calendar.getInstance()

        val startCal = generateCal(start[0].toInt(), start[1].toInt(), (start[2].subSequence(0,2) as String).toInt())
        val endCal = generateCal(end[0].toInt(), end[1].toInt(), (end[2].subSequence(0,2) as String).toInt())
        val difference = (endCal.timeInMillis - startCal.timeInMillis) / (24*60*60*1000) // 전체 기간
        val perDay = (now.timeInMillis - startCal.timeInMillis) / (24*60*60*1000) // 시작지점부터 오늘까지

        if(perDay > 0){
            val percentage = (perDay/difference)*100
            holder.percentText.text = percentage.toInt().toString()+"%"
            holder.per_circle.progress = percentage.toInt()
        }
        else{
            holder.percentText.text = "100%"
            holder.per_circle.progress = 100
        }
    }

    private fun calculateDday(year: Int, month: Int, day: Int): Long {
        val now = Calendar.getInstance()
        val end = Calendar.getInstance()
        end.set(Calendar.YEAR, year)
        end.set(Calendar.MONTH, month - 1)
        end.set(Calendar.DAY_OF_MONTH, day)

        return (end.timeInMillis - now.timeInMillis) / (24 * 60 * 60 * 1000)
    }

    private fun generateCal(year:Int, month:Int, day:Int):Calendar{
        val cal = Calendar.getInstance()
        cal.set(year, month, day)
        return cal
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
    private lateinit var itemClickListener: OnItemClickListener


    interface OnItemLongClickListener {
        fun onLongClick(v: View, position: Int): Boolean {
            return true
        }
    }

    fun setItemLongClickListener(onItemLongClickListener: OnItemLongClickListener) {
        this.itemLongClickListener = onItemLongClickListener
    }

    private lateinit var itemLongClickListener: OnItemLongClickListener

    override fun getItemCount(): Int {
        return scheList?.size ?: 0
    }
}