package com.example.klp.retrofit

import android.util.Log
import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import com.example.klp.data.AppUsageTime
import com.example.klp.data.Article
import com.example.klp.data.ScheduleData
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.io.Serializable
import java.io.StringReader


class RetrofitManager {
    companion object {
        private const val baseUrl =
            //"https://jsonplaceholder.typicode.com/" //http://35.232.144.196:3000
            "http://35.232.144.196:3000"
        val instance = RetrofitManager()
    }

    private val iRetrofit: IRetrofit = RetrofitClient
        .getClient(baseUrl)!!
        .create(IRetrofit::class.java)

    private suspend fun execute(response: Response<ResponseBody>): Serializable {
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

    val array = """[
        { "name": "Joe", "age": 23 },
        { "name": "Jill", "age": 35 }
    ]"""

    fun streamingArray(array: String): ArrayList<ScheduleData> {
        val klaxon = Klaxon()
        JsonReader(StringReader(array)).use { reader ->
            val result = arrayListOf<ScheduleData>()
            reader.beginArray {
                while (reader.hasNext()) {
                    val person = klaxon.parse<ScheduleData>(reader)
                    result.add(person!!)
                }
            }
            return result
        }
    }

    suspend fun getGoals(uid: Int): ArrayList<ScheduleData>? {
        val str = execute(iRetrofit.getGoals(uid)).toString()

        val result = streamingArray(str)
        Log.d("HI", "### $result")
        return result
    }

    suspend fun getPost(id: Int): Any {
        return execute(iRetrofit.getPost(id))
    }

    suspend fun getDangerApp(id: Int, month: Int): Any {
        return execute(iRetrofit.getDangerApp(id, month))
    }

    suspend fun updateDangerApp(id: Int, month: Int, appName: String): Any {
        return execute(iRetrofit.updateDangerApp(id, month, appName))
    }

    suspend fun getStats(
        type: String,
        from: String?,
        to: String?,
        uid: Int?,
        achieved: Int?
    ): Any {
        return execute(iRetrofit.getStats(type, from, to, uid, achieved))
    }

    suspend fun getUsageTime(
        type: String,
        from: String,
        to: String,
        appName: String? = null,
        uid: Int?
    ): Any {
        return execute(iRetrofit.getUsageTime(type, from, to, appName, uid))
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

    fun streamingFormsArray(array: String): ArrayList<Article> {
        val klaxon = Klaxon()
        JsonReader(StringReader(array)).use { reader ->
            val result = arrayListOf<Article>()
            reader.beginArray {
                while (reader.hasNext()) {
                    val person = klaxon.parse<Article>(reader)
                    result.add(person!!)
                }
            }
            return result
        }
    }
    suspend fun getForms(): ArrayList<Article> {
        val str = execute(iRetrofit.getForms()).toString()

        val result = streamingFormsArray(str)
        return result
    }

    suspend fun addSchedule(
        UID: Int, SID: Int, SNAME: String, SDATE1: String, SDATE2: String, SREGULAR: Int,
        STYPE: String, SESTIMATE: Int, SIMPORTANCE: Int, SDETAIL: String, SDONE: Int
    ) {

        val jsonObject = JSONObject()
        jsonObject.put("UID", UID)
        jsonObject.put("SID", SID)
        jsonObject.put("SNAME", SNAME)
        jsonObject.put("SDATE1", SDATE1)
        jsonObject.put("SDATE2", SDATE2)
        jsonObject.put("SREGULAR", SREGULAR)
        jsonObject.put("SWEEKLY", 0)
        jsonObject.put("STYPE", STYPE)
        jsonObject.put("SESTIMATE", SESTIMATE)
        jsonObject.put("SIMPORTANCE", SIMPORTANCE)
        jsonObject.put("SDETAIL", SDETAIL)
        jsonObject.put("SDONE", SDONE)
        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
        execute(iRetrofit.addSchedule(requestBody))


    }

    suspend fun modifySchedule(    UID: Int, SID: Int, SNAME: String, SDATE1: String, SDATE2: String,SREGULAR: Int,
                                STYPE: String, SESTIMATE: Int, SIMPORTANCE: Int, SDETAIL: String,SDONE: Int) {

        val jsonObject = JSONObject()
        jsonObject.put("UID",UID)
        jsonObject.put("SID",SID)
        jsonObject.put("SNAME",SNAME)
        jsonObject.put("SDATE1",SDATE1)
        jsonObject.put("SDATE2",SDATE2)
        jsonObject.put("SREGULAR",SREGULAR)
        jsonObject.put("SWEEKLY",0)
        jsonObject.put("STYPE",STYPE)
        jsonObject.put("SESTIMATE",SESTIMATE)
        jsonObject.put("SIMPORTANCE",SIMPORTANCE)
        jsonObject.put("SDETAIL",SDETAIL)
        jsonObject.put("SDONE",SDONE)
        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
        execute(iRetrofit.modifySchedule(requestBody))


    }

    suspend fun deleteSchedule(UID: Int, SID: Int){
        val jsonObject = JSONObject()
        jsonObject.put("UID", UID)
        jsonObject.put("SID", SID)

        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
        execute(iRetrofit.deleteSchedule(requestBody))

        suspend fun postDangerApp(uid: Int, month: Int, appName: String) {
            val jsonObject = JSONObject()
            jsonObject.put("uid", uid)
            jsonObject.put("month", month)
            jsonObject.put("app_name", appName)

            // Convert JSONObject to String
            val jsonObjectString = jsonObject.toString()

            // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

            execute(iRetrofit.postDangerApp(requestBody))
        }
    }
    suspend fun autoInsert(UID: Int, SID: Int, SNAME: String, SDATE2: String,SREGULAR: Int, SWEEKLY: Int,
                           STYPE: String, SESTIMATE: Int, SIMPORTANCE: Int, SDETAIL: String){
        val jsonObject = JSONObject()
        jsonObject.put("UID",UID)
        jsonObject.put("SID",SID)
        jsonObject.put("SNAME",SNAME)
        jsonObject.put("SDATE2",SDATE2)
        jsonObject.put("SREGULAR",0)
        jsonObject.put("SWEEKLY",0)
        jsonObject.put("STYPE",STYPE)
        jsonObject.put("SESTIMATE",SESTIMATE)
        jsonObject.put("SIMPORTANCE",SIMPORTANCE)
        jsonObject.put("SDETAIL",SDETAIL)
        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
        execute(iRetrofit.autoInsert(requestBody))
    }
}