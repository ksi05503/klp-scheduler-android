package com.example.klp.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.klp.MainActivity
import com.example.klp.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "LoginActivity"
    }

    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLogin()
    }

    private fun initLogin() {
        //로그인 공통 콜백 구성
        val callback: ((OAuthToken?, Throwable?) -> Unit) = { token, error ->
            if (error != null) { //Login Fail
                Log.e(TAG, "로그인 실패 :", error)
            } else if (token != null) { //Login Success
                Log.d(TAG, "로그인 성공, 토큰: " + token.accessToken)
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e(TAG, "사용자 정보 요청 실패", error)
                    } else {
                        Log.i(TAG, "회원번호: " + user!!.id)
                    }
                }
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
        binding.kakaoLoginBtn.setOnClickListener {
            LoginClient.instance.run {
                if (isKakaoTalkLoginAvailable(this@LoginActivity)) {
                    loginWithKakaoTalk(this@LoginActivity, callback = callback)
                } else {
                    loginWithKakaoAccount(this@LoginActivity, callback = callback)
                }
            }
        }

    }

    private fun generateKeyHash() {
        var keyHash = Utility.getKeyHash(this)
        Log.v(TAG, keyHash)
    }
}