package com.example.klp.community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.klp.data.Article
import com.example.klp.databinding.ArticleRowBinding

class ArticleListAdapter(val articleList:ArrayList<Article>):
    RecyclerView.Adapter<ArticleListAdapter.ViewHolder>() {
        inner class ViewHolder(binding:ArticleRowBinding):RecyclerView.ViewHolder(binding.root){
            val formHead = binding.formHeadText
            val likes = binding.likesText
            val commentNum = binding.commentNumText
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ArticleRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return articleList.size
    }
}