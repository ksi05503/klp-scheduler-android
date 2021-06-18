package com.example.klp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.klp.data.ScheduleData


class ScheduleViewModel: ViewModel() {
    //내부에서 설정하는 자료형은 뮤터블로 변경가능하도록 설정

    private val _newSchedules = MutableLiveData<ArrayList<ScheduleData>>()

    //변경되지 않는 데이터를 가저올때 이름을 _ 언더스코어 없이 설정
    //공개적으로 가져오는 변수는 private이 아닌 퍼블릭으로 외부에서 접근가능하도록 설정
    //값을 직접 라이브데이터에 접근하지 않고 뷰모델을 통해 가져올 수 있도록 설정

    val newSchedules: LiveData<ArrayList<ScheduleData>>
        get() = _newSchedules        //get함수 대신 그냥 바로 newSchedule을 가져오면 된다 (수직관계를 위해 이렇게까지)

    init{
        //스케줄 초기화 필요하다면 추가
        _newSchedules.value = ArrayList()
    }


    //임시 데이터
    var data1 = ScheduleData (1,1,"KLP 회의","2021-06-17","2021-06-30",1,1,"회의",3,1,"늦지않기",0)
    var data2 = ScheduleData (1,2,"컴퓨터그래픽스 시험","2021-06-17","2021-06-18",0,-1,"시험",2,3,"망한듯",0)
    var data3 = ScheduleData (1,3,"옷사기","2021-06-17","2021-06-20",0,3,"약속",3,3,"망한듯",1)
    var data4 = ScheduleData (1,4,"자료구조 시험","2021-06-15","2021-06-24",2,6,"시험",2,5,"망한듯",0)
    var data5 = ScheduleData (1,5,"조깅","2021-06-16","2021-07-27",3,5,"운동",2,1,"",0)

    //fun 전체목표 불러오기
    fun loadAllSchedules():ArrayList<ScheduleData>{
        //레트로핏으로 데이터 받아올것
   //     _newSchedules.value =  getSchedules~

//        var scheduleArrayList = ArrayList<ScheduleData>()
//
//        scheduleArrayList.add(data1)
//        scheduleArrayList.add(data2)
//        scheduleArrayList.add(data3)
//
//        return scheduleArrayList
        _newSchedules.value = ArrayList()
        _newSchedules.value!!.apply {
            add(data1)
            add(data2)
            add(data3)
            add(data4)
            add(data5)
        }

        return _newSchedules.value!!
    }

    //fun 오늘 목표 불러오기
    fun loadTodaySchedules():ArrayList<ScheduleData>{
        //레트로핏으로 데이터 받아올것
        //     _newSchedules.value =  getSchedules~


        var scheduleArrayList = ArrayList<ScheduleData>()


        scheduleArrayList.add(data1)

        return scheduleArrayList
    }


    fun loadDoneSchedules():ArrayList<ScheduleData>{
        var scheduleArrayList = ArrayList<ScheduleData>()

        scheduleArrayList.add(data3)

        return scheduleArrayList
    }









/*
model(db) view(layout) control
model <- viewmodel <- view 수직관계를 항상 염두하기

                    얜 뷰에서 쓰는거
                  scheduleViewModel.newSchedule.observe(this, Observer {
                            리사이클러뷰에 나오는 데이터가 바뀔때(추가삭제변경)
                            adapter notify
                            이거 쓰면 알아서 변경이 감지될때마다 리사이클러뷰 업데이트가 된다
                    })  를 이용해서 값이 변경될때마다 목표 프래그먼트의 리사이클러뷰 아이템을 고쳐주면 될듯 하다.
*/


}