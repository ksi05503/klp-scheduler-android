package com.example.klp.utils

import android.icu.text.SimpleDateFormat
import java.lang.Exception
import java.util.*

class DateFormat {
    companion object{
        val CALENDAR_HEADER_FORMAT = "yyyy년 MM월"
        val DAY_FORMAT = "d"

        fun getDate(date:Long, pattern:String):String {
            try{
                val formatter = SimpleDateFormat(pattern, Locale.ENGLISH)
                val d = Date(date)
                return formatter.format(d).uppercase(Locale.ENGLISH)
            }catch(e:Exception){
                return " "
            }
        }
    }
}