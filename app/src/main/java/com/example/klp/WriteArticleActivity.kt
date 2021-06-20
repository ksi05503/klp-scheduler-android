package com.example.klp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.klp.databinding.ActivityWriteArticleBinding
import com.example.klp.retrofit.RetrofitManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class WriteArticleActivity : AppCompatActivity() {
    lateinit var binding: ActivityWriteArticleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.apply {
            cancleBtn.setOnClickListener {
                finish()
            }
            writeBtn.setOnClickListener {
                val form_head = articleName.text.toString()
                val body = articleText.text.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    RetrofitManager.instance.insForm(form_head, body)
                    val resultIntent = Intent()
                    resultIntent.putExtra("form_head", form_head)
                    resultIntent.putExtra("body", body)

                    setResult(100, resultIntent)
                    finish()
                }
            }
        }
    }
}