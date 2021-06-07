package com.example.lab11

import android.graphics.drawable.Drawable
import java.io.Serializable

data class AppData(
    var appLabel: String,
    var appClass: String,
    var appPackageName: String,
    var appIcon: Drawable
) : Serializable {
}