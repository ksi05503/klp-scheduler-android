package com.example.klp.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.klp.data.Article

class ArticleListAdapter(val scheList:List<Article>):
    RecyclerView.Adapter<ArticleListAdapter.ViewHolder>() {
        inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}