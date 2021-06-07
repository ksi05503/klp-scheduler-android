package com.example.klp.adapter


import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.klp.utils.DateFormat


class TextBindingAdapter {

    companion object{
        @BindingAdapter("setCalendarHeaderText")
        fun setCalendarHeaderText(view:TextView, date:Long){
            try{
                if(date!=null){
                    view.setText(DateFormat.getDate(date, DateFormat.CALENDAR_HEADER_FORMAT))
                }
            }catch (e:Exception){
                Log.e("Exception", e.toString())
            }
        }

        @BindingAdapter("setDayText")
        fun setDayText(view:TextView, calendar:Calendar){
            try{
                if(calendar != null){
                    val gregorianCalendar = GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                    view.setText(DateFormat.getDate(gregorianCalendar.timeInMillis, DateFormat.DAY_FORMAT))
                }
            }catch (e:Exception){
                Log.e("Exception", e.toString())
            }
        }
    }




}