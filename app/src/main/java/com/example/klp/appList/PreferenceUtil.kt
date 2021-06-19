package com.example.klp.appList

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    fun getStringSet(key: String = "appList"): Set<String>? {
        return prefs.getStringSet(key, null)
    }

    fun setStringSet(key: String = "appList", value: Set<String>) {
        prefs.edit().putStringSet(key, value).apply()
    }
}
