package com.example.klp.datas

import java.io.Serializable
import android.icu.util.*

data class Schedule(var percent:Int, var category:String, var title:String, var start:Calendar, var end:Calendar):Serializable
