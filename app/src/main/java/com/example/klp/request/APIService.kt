package com.example.klp.request

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


interface APIService {
    /*
       POST METHOD
    */
    // Raw JSON
    @POST("/api/v1/create")
    suspend fun createEmployee(@Body requestBody: RequestBody): Response<ResponseBody>

    /*****************************************************************************************************************************************************/


    /*
        GET METHOD
    */

    @GET("/api/v1/employees")
    suspend fun getEmployees(): Response<ResponseBody>


    // Request using @Query (e.g https://example.com/diary?uid=""&enter_date="")
    @GET("/diary")
    suspend fun getDiary(
        @Query("uid") uid: Int,
        @Query("enter_date") enter_date: String,
    ): Response<ResponseBody>


    // Request using @Query (e.g https://example.com/usageTime?from=“”&to=“”[&app_name=“”][&uid=“”])
    @GET("/usageTime")
    suspend fun getUsageTime(
        @Query("from") from: Int,
        @Query("to") to: Int,
        @Query("app_name") app_name: String?,
        @Query("uid") uid: String?
    ): Response<ResponseBody>


    // Request using @Path (e.g https://reqres.in/api/users/53 - This URL is just an example, it's not working)
    @GET("/api/users/{Id}")
    suspend fun getEmployee(@Path("Id") employeeId: String): Response<ResponseBody>

    /*****************************************************************************************************************************************************/

}