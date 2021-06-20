package com.example.klp.retrofit

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


interface IRetrofit {
    /*****************************************************************************************************************************************************/
    /*
        POST METHOD
    */

    // JSON
    @POST("/app/usageTime")
    suspend fun postAppUsageTime(@Body requestBody: RequestBody): Response<ResponseBody>

    @POST("/app/danger")
    suspend fun postDangerApp(@Body requestBody: RequestBody): Response<ResponseBody>

    // Form Data
    @Multipart
    @POST("/post")
    suspend fun f2(@PartMap map: HashMap<String?, RequestBody?>): Response<ResponseBody>


    @POST("/addSchedule")
    suspend fun addSchedule(@Body requestBody: RequestBody): Response<ResponseBody>

    @POST("/autoInsert")
    suspend fun autoInsert(@Body requestBody: RequestBody): Response<ResponseBody>


    @POST("/modifySchedule")
    suspend fun modifySchedule(@Body requestBody: RequestBody): Response<ResponseBody>
    /*****************************************************************************************************************************************************/
    /*
        GET METHOD
    */
    // /getForms
    @GET("/getForms")
    suspend fun getForms(
    ): Response<ResponseBody>

    @GET("/posts/{id}")
    suspend fun getPost(
        @Path("id") id: Int
    ): Response<ResponseBody>

    // /usageTime?from=“”&to=“”[&app_name=“”][&uid=“”]
    @GET("/app/getUsageTime")
    suspend fun getUsageTime(
        @Query("type") type: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("app_name") app_name: String?,
        @Query("uid") uid: Int?
    ): Response<ResponseBody>

    // /diary?uid=""&enter_date=""
    @GET("/diary")
    suspend fun getDiary(
        @Query("UID") UID: Int,
        @Query("ENTER_DATE") ENTER_DATE: String,
    ): Response<ResponseBody>

    // /diary?uid=""&enter_date=""
    @GET("/app/danger")
    suspend fun getDangerApp(
        @Query("uid") uid: Int,
        @Query("month") month: Int,
    ): Response<ResponseBody>

    // /getAllGoals?uid=""
    @GET("/getAllGoals")
    suspend fun getGoals(
        @Query("UID") uid: Int,
    ): Response<ResponseBody>


    // /schedule?type=(‘object’|‘count’|‘mean’|’std‘)[from=“”&to=“”][&uid=“”][&achieved=(‘true’|‘false’)]
    @GET("/schedule")
    suspend fun getStats(
        @Query("type") type: String,
        @Query("from") from: String?,
        @Query("to") to: String?,
        @Query("uid") uid: Int?,
        @Query("achieved") achieved: Int?,

        ): Response<ResponseBody>

    /*****************************************************************************************************************************************************/
    /*
       PATCH METHOD
    */

    @PATCH("/app/danger")
    suspend fun updateDangerApp(
        @Query("uid") uid: Int,
        @Query("month") month: Int,
        @Query("app_name") app_name: String,
    ): Response<ResponseBody>


    /*****************************************************************************************************************************************************/
    /*
       PUT METHOD
    */

    @PUT("/api/users/2")
    suspend fun updateB(@Body requestBody: RequestBody): Response<ResponseBody>


    /*****************************************************************************************************************************************************/
    /*
       DELETE METHOD
    */

    @DELETE("/typicode/demo/posts/1")
    suspend fun deleteC(): Response<ResponseBody>

    @POST("/deleteSchedule")
    suspend fun deleteSchedule(@Body requestBody: RequestBody): Response<ResponseBody>


    /*****************************************************************************************************************************************************/


}
