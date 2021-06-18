package com.example.klp.data

data class ScheduleData(
    var uid: Int, var sid: Int, var sname: String, var sdate1: String, var sdate2: String,
    var sregular: Int, var sweekly: Int,
    var stype: String, var sestimate: Int, var simportance: Int,
    var sdetail: String, var sdone: Int, val achievedRate: Int? = null
)