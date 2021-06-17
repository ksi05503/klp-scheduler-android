package com.example.klp.data

import java.io.Serializable
import android.icu.util.*

data class ScheduleData(var uid:String, var sid:Int, var sname:String, var sdate:String,
                        var sregular:Int, var sweekly:String,
                        var stype:String, var sestimate:Int, var simportance:Int,
                        var sdetail:String, var sdone:Int) {
}