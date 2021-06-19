package com.example.klp.community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.klp.data.Article
import com.example.klp.databinding.ActivityArticleBinding

class ArticleActivity : AppCompatActivity() {
    lateinit var binding:ActivityArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val Article = intent.getSerializableExtra("article") as Article
        binding.apply {
            formHead.text = Article.FORM_HEAD
            likes.text = "[좋아요 ${Article.FORM_LIKE.toString()}개]"
            bodyText.text = Article.BODY
        }
    }
}