package com.example.klp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.klp.databinding.ActivityArticleBinding

class ArticleActivity : AppCompatActivity() {
    lateinit var binding:ActivityArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}