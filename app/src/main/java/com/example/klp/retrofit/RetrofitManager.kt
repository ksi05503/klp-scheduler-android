package com.example.klp.retrofit

import android.util.Log
import com.example.klp.data.AppUsageTime
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response


class RetrofitManager {
    companion object {
        private const val baseUrl =
            "https://jsonplaceholder.typicode.com/" //http://35.232.144.196:3000
        val instance = RetrofitManager()
    }

    private val iRetrofit: IRetrofit = RetrofitClient
        .getClient(baseUrl)!!
        .create(IRetrofit::class.java)

    private suspend fun execute(response: Response<ResponseBody>): Any {
        val value = GlobalScope.async(Dispatchers.IO) {
            // Do the GET request and get response
            if (response.isSuccessful) {
                // Convert raw JSON to pretty JSON using GSON library
                val gson = GsonBuilder().setPrettyPrinting().create()
                return@async gson.toJson(
                    JsonParser.parseString(
                        response.body()
                            ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                    )
                )
            } else {
                Log.e("HI", response.code().toString())
            }
        }.await()
        return value
    }

    suspend fun getDiary(uid: Int, enterDate: String): Any {
        return execute(iRetrofit.getDiary(uid, enterDate))
    }

    suspend fun getPost(id: Int): Any {
        return execute(iRetrofit.getPost(id))
    }

    suspend fun postAppUsageTime(uid: Int, enterDate: String, appUsageList: Array<AppUsageTime>) {
        val jsonObject = JSONObject()
        jsonObject.put("uid", uid)
        jsonObject.put("enter_date", enterDate)
        jsonObject.put("app_list", appUsageList)

        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        execute(iRetrofit.postAppUsageTime(requestBody))
    }
}