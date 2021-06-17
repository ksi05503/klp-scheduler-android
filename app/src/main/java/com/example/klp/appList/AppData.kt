package com.example.klp.appList

import android.graphics.drawable.Drawable
import java.io.Serializable

data class AppData(
    var appLabel: String,
    var appPackageName: String,
    var appIcon: Drawable? = null,
    var usageTime: Int = 0
) : Serializable {
}