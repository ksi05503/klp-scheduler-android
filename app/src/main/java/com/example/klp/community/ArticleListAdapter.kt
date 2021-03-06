package com.example.klp.community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.klp.data.Article
import com.example.klp.databinding.ArticleRowBinding

class ArticleListAdapter(var articleList: ArrayList<Article>) :
    RecyclerView.Adapter<ArticleListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun OnItemClick(holder: ViewHolder, view: View, article: Article, position: Int)
    }

    var itemClickListener1: OnItemClickListener? = null
    var itemClickListener2: OnItemClickListener? = null

    inner class ViewHolder(binding: ArticleRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val formHead = binding.formHeadText
        val likes = binding.likesText
        val body = binding.body

        init {
            binding.articleRowLay.setOnClickListener {
                itemClickListener1?.OnItemClick(
                    this,
                    it,
                    articleList[adapterPosition],
                    adapterPosition
                )
            }
            binding.likeBtn.setOnClickListener {
                itemClickListener2?.OnItemClick(
                    this,
                    it,
                    articleList[adapterPosition],
                    adapterPosition
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setData(articles: ArrayList<Article>) {
        articleList = articles
        notifyDataSetChanged()
    }
    fun addArticle(article: Article){
        articleList.add(article)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ArticleRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.formHead.text = articleList[position].FORM_HEAD
        holder.likes.text = "????????? ${articleList[position].FORM_LIKE}???"
        holder.body.text = articleList[position].BODY
    }

    override fun getItemCount(): Int {
        return articleList.size
    }
}