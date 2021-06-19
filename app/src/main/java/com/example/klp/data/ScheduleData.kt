package com.example.klp.data

data class ScheduleData(
    var UID: Int, var SID: Int, var SNAME: String, var SDATE1: String, var SDATE2: String,
    var SREGULAR: Int, var STYPE: String, var SESTIMATE: Int,
    var SIMPORTANCE: Int, var SDETAIL: String,
    var SDONE: Int
)