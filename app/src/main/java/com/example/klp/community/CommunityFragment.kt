package com.example.klp.community

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.klp.R
import com.example.klp.data.Article
import com.example.klp.databinding.FragmentCommunityBinding


class CommunityFragment : Fragment() {
    var binding:FragmentCommunityBinding?=null
    var adapter:ArticleListAdapter?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommunityBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter= ArticleListAdapter(ArrayList<Article>())

        binding!!.apply {
            val fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)

            recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
            adapter!!.itemClickListener = object : ArticleListAdapter.OnItemClickListener{
                override fun OnItemClick(
                    holder: ArticleListAdapter.ViewHolder,
                    view: View,
                    article: Article,
                    position: Int
                ) {
                    val body = view.findViewById<TextView>(R.id.body)
                    if(body.isVisible){
                        body.visibility = View.GONE
                    }
                    else{
                        body.visibility = View.VISIBLE
                        body.startAnimation(fadeIn)
                    }
                }
            }

            //테스트
            adapter!!.articleList.add(Article(1, 1, "테스트", "밤새 테스트 하는 중", 999))
            adapter!!.notifyDataSetChanged()

            recyclerView.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}