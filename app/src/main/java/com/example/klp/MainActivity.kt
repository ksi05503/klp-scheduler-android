package com.example.klp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.klp.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    val fragArr = arrayListOf<String>("전체 목표", "오늘 실천", "나의 통계", "커뮤니티")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }


    private fun init(){
        binding.viewPager.adapter = MyFragStateAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager){
            tab, position ->
            tab.text = fragArr[position]
        }.attach()

    }
}