package com.example.klp.retrofit

import com.example.klp.ScheduleData
import retrofit2.Call
import retrofit2.http.*


interface IRetrofit {
    //   @GET("/getSchedule")
    //   fun getSchedule(@Query("name") name: String): Call<ScheduleData>
    //   @GET("/getScheduleid")
    //   fun getSchedule(@Query("sname") sname: String): Call<Int>

    // http//,,baseURL,,/addSchedule/?query=""

//    @FormUrlEncoded
//    @POST("/addSchedule")
//    fun addSchedule(@Query("uid") uid: Int,):Call<ScheduleData>   //sid 는 자동으로 누적

    @FormUrlEncoded
    @POST("/addSchedule")
    fun addSchedule(@Query("schedule") schedule: ScheduleData,):Call<Boolean>   //sid 는 자동으로 누적

//    @FormUrlEncoded
//   @PUT("/modifySchedule")
//    fun modifySchedule(@Path("id")id: String,
//                       @Field("content")content: String): Call<ScheduleData>
//
//    @DELETE("/deleteSchedule")
//    fun deleteSchedule(@Path("id")id: String): Call<ScheduleData>
}
