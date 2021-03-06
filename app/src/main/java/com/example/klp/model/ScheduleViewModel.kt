package com.example.klp.model

import android.icu.util.Calendar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.klp.customclass.handleSdate
import com.example.klp.data.ScheduleData


class ScheduleViewModel : ViewModel() {
    //내부에서 설정하는 자료형은 뮤터블로 변경가능하도록 설정

    val cal = Calendar.getInstance()
    val todayInt = cal.get(Calendar.YEAR)*10000+ (cal.get(Calendar.MONTH)+1)*100 + cal.get(Calendar.DATE)

    val _newSchedules = MutableLiveData<ArrayList<ScheduleData>>()

    //변경되지 않는 데이터를 가저올때 이름을 _ 언더스코어 없이 설정
    //공개적으로 가져오는 변수는 private이 아닌 퍼블릭으로 외부에서 접근가능하도록 설정
    //값을 직접 라이브데이터에 접근하지 않고 뷰모델을 통해 가져올 수 있도록 설정
    private var type = 0
    val newSchedules: LiveScheduleData<ArrayList<ScheduleData>>
        get() {
            val liveData = LiveScheduleData<ArrayList<ScheduleData>>()
            val tmp = ArrayList<ScheduleData>()
            if (type == 0) {
                val data = ArrayList(_newSchedules?.value?.filter { item -> item.SDONE == 0 && handleSdate(item.SDATE2.split('T')[0]).dayInt >= todayInt }
                    ?: tmp)
                liveData.setData(data)
            } else {
                val data = ArrayList(_newSchedules?.value?.filter { item -> item.SDONE == 1 || handleSdate(item.SDATE2.split('T')[0]).dayInt < todayInt }
                    ?: tmp)
                liveData.setData(data)
            }

            return liveData
        }

    fun setDone() {
        type = 1
    }

    fun setOngoing() {
        type = 0
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