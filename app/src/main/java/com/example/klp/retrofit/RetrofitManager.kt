package com.example.klp.retrofit

import android.content.ContentValues.TAG
import android.util.Log
import com.example.klp.ScheduleData
import com.example.klp.utils.API
import retrofit2.Call
import retrofit2.Response



class RetrofitManager {
    companion object{
        val instance = RetrofitManager()
    }

    //레트로핏 인터페이스 가져오기
    private val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)
        ?.create(IRetrofit::class.java)

    //스케줄 추가 api 호출
    fun addSchedule(schedule: ScheduleData) {

        val call = iRetrofit?.addSchedule(schedule = schedule).let{
            it
        }?: return

        call.enqueue(object: retrofit2.Callback<Boolean>{
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                Log.d(TAG,"RetrofitManager - onResponse() called/ t: ${response.body()}")
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.d(TAG,"RetrofitManager - onFailure() called/ t: $t")
            }

        })

    }

/*
            override fun onResponse(call: Call<ScheduleData>, response: Response<ScheduleData>) {
                Log.d(TAG,"RetrofitManager - onResponse() called/ t: ${response.raw()}")

                completion(response.raw().toString())
            }

            override fun onFailure(call: Call<ScheduleData>, t: Throwable) {
                Log.d(TAG,"RetrofitManager - onFailure() called/ t: $t")
            }
 */
}