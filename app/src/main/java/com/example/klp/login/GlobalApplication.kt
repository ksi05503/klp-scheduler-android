package com.example.klp.login

import android.app.Application
import com.example.klp.R
import com.example.klp.appList.PreferenceUtil
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate() {
        super.onCreate()
        prefs = PreferenceUtil(applicationContext)
        KakaoSdk.init(this, getString(R.string.kakao_app_key))
    }
}