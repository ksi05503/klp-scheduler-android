package com.example.klp.wholegoal

import android.content.Context
import android.icu.util.Calendar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.klp.customclass.handleSdate
import com.example.klp.data.ScheduleData
import com.example.klp.databinding.GoalRowBinding
import com.example.klp.retrofit.RetrofitManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.round


class GoalFragRecyclerViewAdapter(var scheList:ArrayList<ScheduleData>?):RecyclerView.Adapter<GoalFragRecyclerViewAdapter.ViewHolder>() {


    inner class ViewHolder(binding: GoalRowBinding):RecyclerView.ViewHolder(binding.root){
        val sname: TextView = binding.sname
        val sdate: TextView = binding.sdate
        val sregular:TextView = binding.sregular
        val dDay: TextView = binding.dDay
        val stype: TextView = binding.stype
        val per_circle: ProgressBar = binding.percentCircle
        val doneCheckBox: CheckBox = binding.doneCheckBox


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
        val cal = Calendar.getInstance()
        val todayInt = cal.get(Calendar.YEAR)*10000+ (cal.get(Calendar.MONTH)+1)*100 + cal.get(Calendar.DATE)

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        holder.itemView.setOnLongClickListener {
            itemLongClickListener.onLongClick(it, position)
        }

        holder.doneCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){

                CoroutineScope(Dispatchers.Main).launch{
                    RetrofitManager.instance.modifySchedule(1759543463,scheList!!.get(position).SID,scheList!!.get(position).SNAME,scheList!!.get(position).SDATE1.split('T')[0],scheList!!.get(position).SDATE2.split('T')[0],0,scheList!!.get(position).STYPE,scheList!!.get(position).SESTIMATE,scheList!!.get(position).SIMPORTANCE,scheList!!.get(position).SDETAIL,1)
                    //????????????????????? ??????????????? ????????????????????????
                    holder.doneCheckBox.isChecked = scheList!![position].SDONE==1 || handleSdate(scheList!!.get(position).SDATE2.split('T')[0]).dayInt < todayInt
                    handleSdate(scheList!!.get(position).SDATE2.split('T')[0]).dayInt < todayInt
                }
            }else{
                CoroutineScope(Dispatchers.Main).launch{
                    RetrofitManager.instance.modifySchedule(1759543463,scheList!!.get(position).SID,scheList!!.get(position).SNAME,scheList!!.get(position).SDATE1.split('T')[0],scheList!!.get(position).SDATE2.split('T')[0],0,scheList!!.get(position).STYPE,scheList!!.get(position).SESTIMATE,scheList!!.get(position).SIMPORTANCE,scheList!!.get(position).SDETAIL,0)
                }
            }
        }



        //????????????????????? ??????????????? ????????????????????????
        holder.doneCheckBox.isChecked = scheList!![position].SDONE==1 || handleSdate(scheList!!.get(position).SDATE2.split('T')[0]).dayInt < todayInt
        handleSdate(scheList!!.get(position).SDATE2.split('T')[0]).dayInt < todayInt



        //??????????????????
        val hsd = handleSdate(scheList!![position].SDATE2)
        holder.sname.text = scheList!![position].SNAME
        holder.sdate.text = "${hsd.year}??? ${hsd.month}??? ${hsd.day}???"
        holder.stype.text = scheList!![position].STYPE

        //?????? ??????
        when(scheList!![position].SREGULAR){
            0->holder.sregular.text = ""
            1->holder.sregular.text = "??????"
            2->holder.sregular.text = "??????"
            3->holder.sregular.text = "??????"
        }

        //DDAY ??????
        val ymdStr = scheList!![position].SDATE2.split("-")
        val year = ymdStr[0].toInt()
        val month = ymdStr[1].toInt()
        val day = (ymdStr[2].subSequence(0, 2) as String).toInt()

        val dDayValue = calculateDday(year, month, day)
        if(dDayValue > 0){
            holder.dDay.text = "D-"+abs(dDayValue).toString()

            val start = scheList!![position].SDATE1.split("-")
            val end = scheList!![position].SDATE2.split("-")

            val nowCal = Calendar.getInstance()
            val startCal = generateCal(start[0].toInt(), start[1].toInt(), (start[2].subSequence(0,2) as String).toInt())
            val endCal = generateCal(end[0].toInt(), end[1].toInt(), (end[2].subSequence(0,2) as String).toInt())

            val onGoing = (nowCal.timeInMillis - startCal.timeInMillis).toDouble()
            val entire = (endCal.timeInMillis - startCal.timeInMillis).toDouble()

            if(onGoing > 0){
                val difference = onGoing/entire // ?????? ??????
                holder.per_circle.progress = (difference*100).toInt()
            }
            else{
                holder.per_circle.progress = 0
            }

        }
        else if(dDayValue==0L){
            holder.dDay.text = "D-DAY"
            holder.per_circle.progress = 100
        } else {
            holder.dDay.text = "D+" + abs(dDayValue).toString()
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
        cal.set(year, month - 1, day)
        return cal
    }

    // (2) ????????? ???????????????
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    // (3) ???????????? ?????? ??? ????????? ??????
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    // (4) setItemClickListener??? ????????? ?????? ??????
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