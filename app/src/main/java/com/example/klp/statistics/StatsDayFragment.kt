package com.example.klp.statistics

import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.klp.appList.AppData
import com.example.klp.databinding.FragmentStatsDayBinding
import com.example.klp.request.APIService
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import java.util.*
import kotlin.Comparator

class StatsDayFragment : Fragment() {
    companion object {
        val TAG = "retrofit"
    }

    private var binding: FragmentStatsDayBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatsDayBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun init() {

        if (!checkForPermission()) {
            Log.i("HH", "The user may not allow the access to apps usage. ")
            Toast.makeText(
                requireActivity(),
                "Failed to retrieve app usage statistics. " +
                        "You may need to enable access for this app through " +
                        "Settings > Security > Apps with usage access",
                Toast.LENGTH_LONG
            ).show()
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        } else {
            // We have the permission. Query app usage stats.
            //postMethod()
        }


    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkForPermission(): Boolean {
        val appOps = requireActivity().getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.unsafeCheckOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(), ContextWrapper(requireActivity()).packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    private fun getAppUsageStats(year: Int, month: Int, date: Int): List<AppData> {
        val beginTime = Calendar.getInstance()
        beginTime.set(year, month - 1, date, 0 - 9, 0, 0)
        val endTime = Calendar.getInstance()
        endTime.set(year, month - 1, date, 23 - 9, 59, 59)
        val usageStatsManager =
            requireActivity().getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager // 2
        val result = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, beginTime.timeInMillis, endTime.timeInMillis// 3
        )
        result.sortWith(Comparator { right, left ->
            compareValues(left.lastTimeUsed, right.lastTimeUsed)
        })

        return (result.filter { it.totalTimeInForeground > 0 }.map {
            AppData(
                it.packageName,
                it.packageName,
                usageTime = (it.totalTimeInForeground / 1000 / 60).toInt()
            )
        })
    }

    private fun postMethod() {

        // Uncomment the one you want to test, and comment the others

        rawJSON()

        // urlEncoded()

        // formData()

    }

    private fun rawJSON() {

        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://dummy.restapiexample.com")
            .build()

        // Create Service
        val service = retrofit.create(APIService::class.java)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            } else {
                val jsonObject = JSONObject()
                jsonObject.put("uid", user!!.id)
                jsonObject.put("date", "2021-06-07")
                val list = getAppUsageStats(2021, 6, 7)
                jsonObject.put("app_name", list[0].appPackageName)
                jsonObject.put("usage_time", list[0].usageTime)

                // Convert JSONObject to String
                val jsonObjectString = jsonObject.toString()

                // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
                val requestBody =
                    jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

                CoroutineScope(Dispatchers.IO).launch {
                    // Do the POST request and get response
                    val response = service.createEmployee(requestBody)

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {

                            // Convert raw JSON to pretty JSON using GSON library
                            val gson = GsonBuilder().setPrettyPrinting().create()
                            val prettyJson = gson.toJson(
                                JsonParser.parseString(
                                    response.body()
                                        ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                                )
                            )
                            Log.d("Pretty Printed JSON :", prettyJson)

                            Toast.makeText(requireActivity(), "전송완료", Toast.LENGTH_SHORT).show()

                        } else {

                            Log.e(TAG, response.code().toString())

                        }
                    }
                }
            }
        }
        // Create JSON using JSONObject

    }
    private fun getMethod() {

        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://dummy.restapiexample.com")
            .build()

        // Create Service
        val service = retrofit.create(APIService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            /*
             * For @Query: You need to replace the following line with val response = service.getEmployees(2)
             * For @Path: You need to replace the following line with val response = service.getEmployee(53)
             */

            // Do the GET request and get response
            val response = service.getEmployees()

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    // Convert raw JSON to pretty JSON using GSON library
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()
                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                        )
                    )
                    Log.d("Pretty Printed JSON :", prettyJson)


                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())

                }
            }
        }
    }
    private fun filterAppUsageStats(usageStats: MutableList<UsageStats>) {
        usageStats.sortWith(Comparator { right, left ->
            compareValues(left.lastTimeUsed, right.lastTimeUsed)
        })

        usageStats.filter { it.totalTimeInForeground > 0 }.forEach {
            Log.d(
                TAG,
                "packageName: ${it.packageName}, lastTimeUsed: ${Date(it.lastTimeUsed)}, lastTimeStamp: ${it.lastTimeStamp}, " +
                        "totalTimeInForeground: ${it.totalTimeInForeground / 1000 / 60}분 = ${it.totalTimeInForeground / 1000 / 60 / 60}시간"
            )
        }
    }
}