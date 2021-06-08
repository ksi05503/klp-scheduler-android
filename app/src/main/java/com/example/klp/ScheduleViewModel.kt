package com.example.klp

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.klp.retrofit.RetrofitManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

enum class ActionType{
    Add,Delete,Modify
}
class ScheduleViewModel: ViewModel() {
    //내부에서 설정하는 자료형은 뮤터블로 변경가능하도록 설정

    private val _newSchedule = MutableLiveData<ScheduleData>()

    //변경되지 않는 데이터를 가저올때 이름을 _ 언더스코어 없이 설정
    //공개적으로 가져오는 변수는 private이 아닌 퍼블릭으로 외부에서 접근가능하도록 설정
    //값을 직접 라이브데이터에 접근하지 않고 뷰모델을 통해 가져올 수 있도록 설정

    val newSchedule: LiveData<ScheduleData>
        get() = _newSchedule        //get함수 대신 그냥 바로 newSchedule을 가져오면 된다

    init{
        //스케줄 초기화 필요하다면 추가
    }

    fun handleScheduleDB(actionType: ActionType, input: ScheduleData){
        _newSchedule.value = input

        when(actionType){
            ActionType.Add ->
                RetrofitManager.instance.addSchedule(schedule = input)
/*            ActionType.Delete ->
                //db에서 삭제
            ActionType.Modify ->
                //db에 변경
*/       }


    }
/*

                    scheduleViewModel.newSchedule.observe(this, Observer {

                    })  를 이용해서 값이 변경될때마다 목표 프래그먼트의 리사이클러뷰 아이템을 고쳐주면 될듯 하다.
*/


}