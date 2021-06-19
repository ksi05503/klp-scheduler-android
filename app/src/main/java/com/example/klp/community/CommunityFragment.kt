package com.example.klp.community

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.klp.WriteArticleActivity
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
            recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))

            recyclerView.adapter = adapter
            //테스트
            adapter!!.articleList.add(Article(1, 1, "테스트", "테스트", 5, 5))
            adapter!!.notifyDataSetChanged()

            addArticleBtn.setOnClickListener {
                val intent = Intent(requireContext(), WriteArticleActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}