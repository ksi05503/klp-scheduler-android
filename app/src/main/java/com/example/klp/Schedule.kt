package com.example.klp

import java.io.Serializable
import android.icu.util.*


//이 데이터는 안쓸 예정
data class Schedule(var percent:Int, var category:String, var title:String, var start:Calendar, var end:Calendar):Serializable
