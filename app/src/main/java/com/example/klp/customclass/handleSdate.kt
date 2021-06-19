package com.example.klp.customclass

class handleSdate(dayString:String) {

    var dayString =""
    var year:Int =-1
    var month:Int =-1
    var day:Int =-1
    var syear =""
    var smonth=""
    var sday=""

    init {
        syear = dayString.split('-')[0]
        smonth = dayString.split('-')[1]
        sday = dayString.split('-')[2]

        year = dayString.split('-')[0].toInt()
        month = dayString.split('-')[1].toInt()
        day = dayString.split('-')[2].toInt()
    }

}