package com.example.klp.customclass

class handleSdate(dayString:String) {
    var dayString =""
    var year:Int =-1
    var month:Int =-1
    var day:Int =-1
    var syear =""
    var smonth=""
    var sday=""

    var dayInt =-1

    init {
        syear = dayString.split('-')[0]
        smonth = dayString.split('-')[1]
        sday = dayString.split('-')[2].subSequence(0,2) as String

        year = dayString.split('-')[0].toInt()

        if(year<10){
            syear = "0${year}"
        }

        month = dayString.split('-')[1].toInt()
        day = (dayString.split('-')[2].subSequence(0,2) as String).toInt()

        if(year<10){
            syear = "0${year}"
        }

        dayInt = year*10000+ month*100 + day
    }

}