package com.example.klp.customclass

class handleSdate(str:String) {

    private var dateString : String = ""



    var dayString =""
    var year:Int =-1
    var month:Int =-1
    var day:Int =-1

    var timeString = ""
    var hour:Int = -1
    var minute:Int =-1

    init {
        dateString = str

        if (dateString.split(' ').size == 2){
            dayString = dateString.split(' ')[0]
            timeString = dateString.split(' ')[1]
        }else{
            dayString = "0000-00-00"
            timeString = "00:00"
        }

        year = dayString.split('-')[0].toInt()
        month = dayString.split('-')[1].toInt()
        day = dayString.split('-')[2].toInt()

        hour = timeString.split(':')[0].toInt()
        minute = timeString.split(':')[1].toInt()

    }






}