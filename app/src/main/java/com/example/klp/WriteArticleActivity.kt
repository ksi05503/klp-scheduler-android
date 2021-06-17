package com.example.klp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.klp.databinding.ActivityWriteArticleBinding

class WriteArticleActivity : AppCompatActivity() {
    lateinit var binding: ActivityWriteArticleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init(){
        binding.apply {
            cancleBtn.setOnClickListener {
                finish()
            }
        }
    }
}