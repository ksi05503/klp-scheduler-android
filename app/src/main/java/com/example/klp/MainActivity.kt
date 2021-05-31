package com.example.klp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.klp.databinding.ActivityMainBinding
import com.example.klp.statistics.StatsDayFragment
import com.example.klp.statistics.StatsMonthFragment
import com.example.klp.statistics.StatsWeekFragment
import com.example.klp.statistics.ViewModelForStatsTab
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private val viewModelForStatsTab: ViewModelForStatsTab by viewModels()
    private val statsFragments = arrayOf(
        StatsDayFragment(),
        StatsWeekFragment(),
        StatsMonthFragment()
    )
    lateinit var binding: ActivityMainBinding
    val fragArr = arrayListOf<String>("전체 목표", "오늘 실천", "나의 통계", "커뮤니티")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }


    private fun init() {
        binding.viewPager.adapter = MyFragStateAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = fragArr[position]
        }.attach()
        viewModelForStatsTab.selectedTab.observe(this, {
            val fragment = supportFragmentManager.beginTransaction()
            var substitute: Fragment = when (it) {
                0 -> statsFragments[0]
                1 -> statsFragments[1]
                else -> statsFragments[2]
            }
            fragment.replace(R.id.statistics_frameLayout, substitute)
            fragment.commit()
        })
    }
}