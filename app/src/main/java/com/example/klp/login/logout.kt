package com.example.klp.login

import android.util.Log
import com.kakao.sdk.user.UserApiClient

fun logout() {
    UserApiClient.instance.logout { err ->
        if (err != null) Log.e("LoginActivity", "로그아웃 실패", err)
        else {
            Log.d("LoginActivity", "로그아웃 성공")
            //startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}