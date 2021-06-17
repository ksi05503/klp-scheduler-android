package com.example.klp.data

import java.io.Serializable
import android.icu.util.*
import com.example.klp.utils.Category

data class Schedule(var percent:Int, var category:Category, var title:String, var start:Calendar, var end:Calendar):Serializable
