package com.example.klp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.klp.data.Article


class ArticleViewModel : ViewModel() {
    private val _articles = MutableLiveData<ArrayList<Article>>()

    private var type = 0
    val articles: LiveData<ArrayList<Article>>
        get() = _articles
}