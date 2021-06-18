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


    // Form Data
    @Multipart
    @POST("/post")
    suspend fun f2(@PartMap map: HashMap<String?, RequestBody?>): Response<ResponseBody>

    /*****************************************************************************************************************************************************/
    /*
        GET METHOD
    */
    @GET("/posts/{id}")
    suspend fun getPost(
        @Path("id") id: Int
    ): Response<ResponseBody>

    // /usageTime?from=“”&to=“”[&app_name=“”][&uid=“”]
    @GET("/usageTime")
    suspend fun getUsageTime(
        @Query("from") from: Int,
        @Query("to") to: Int,
        @Query("app_name") app_name: String?,
        @Query("uid") uid: String?
    ): Response<ResponseBody>

    // /diary?uid=""&enter_date=""
    @GET("/diary")
    suspend fun getDiary(
        @Query("uid") uid: Int,
        @Query("enter_date") enter_date: String,
    ): Response<ResponseBody>


    /*****************************************************************************************************************************************************/
    /*
       PATCH METHOD
    */

    @PATCH("/example")
    suspend fun updateA(
        @Query("uid") uid: Int,
        @Body requestBody: RequestBody
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


    /*****************************************************************************************************************************************************/


}
