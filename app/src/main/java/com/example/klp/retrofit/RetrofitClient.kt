package com.example.klp.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//싱글턴 (메모리하나만씀)
object RetrofitClient{
    //레트로핏 클라이언트 선언
    private var retrofitClient: Retrofit? = null

    //레트로핏 클라이언트 가져오기
    fun getClient(baseUrl: String): Retrofit?{
        if(retrofitClient == null){

            //레트로핏 빌더로 인스턴스 생성
            retrofitClient = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofitClient
    }
}