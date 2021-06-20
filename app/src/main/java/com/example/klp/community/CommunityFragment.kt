package com.example.klp.community

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.klp.R
import com.example.klp.WriteArticleActivity
import com.example.klp.data.Article
import com.example.klp.databinding.FragmentCommunityBinding
import com.example.klp.model.ArticleViewModel
import com.example.klp.retrofit.RetrofitManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CommunityFragment : Fragment() {
    private val articleViewModel: ArticleViewModel by activityViewModels()

    var adapter: ArticleListAdapter? = null
    fun addArticle(article: Article) {
        adapter!!.addArticle(article)
    }


    var binding: FragmentCommunityBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommunityBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ArticleListAdapter(ArrayList<Article>())
        articleViewModel.articles.observe(viewLifecycleOwner, {
            adapter!!.setData(it)
        })
        binding!!.apply {
            val fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)

            recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerView.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
            adapter!!.itemClickListener1 = object : ArticleListAdapter.OnItemClickListener {
                override fun OnItemClick(
                    holder: ArticleListAdapter.ViewHolder,
                    view: View,
                    article: Article,
                    position: Int
                ) {
                    val body = view.findViewById<TextView>(R.id.body)
                    if (body.isVisible) {
                        body.visibility = View.GONE
                    } else {
                        body.visibility = View.VISIBLE
                        body.startAnimation(fadeIn)
                    }
                }
            }

            adapter!!.itemClickListener2 = object : ArticleListAdapter.OnItemClickListener {
                override fun OnItemClick(
                    holder: ArticleListAdapter.ViewHolder,
                    view: View,
                    article: Article,
                    position: Int
                ) {
                    val btn = view.findViewById<ImageButton>(R.id.likeBtn)
                    var text = holder.likes.text
                    if (btn.isSelected) {
                        val prev = text.split(" ")[1].split("개")[0].toInt()
                        holder.likes.text = "좋아요 ${prev - 1}개"
                        btn.isSelected = false
                    } else {
                        val prev = text.split(" ")[1].split("개")[0].toInt()
                        holder.likes.text = "좋아요 ${prev + 1}개"
                        btn.isSelected = true
                    }
                }
            }

            addArticleBtn.setOnClickListener {
                val intent = Intent(requireContext(), WriteArticleActivity::class.java)
                startActivityForResult(intent, 100)
            }

            //테스트
            CoroutineScope(Dispatchers.Main).launch {
                val forms = RetrofitManager.instance.getForms()
                forms.forEach { item -> adapter!!.articleList.add(item) }
                adapter!!.notifyDataSetChanged()
            }

            recyclerView.adapter = adapter

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            if (resultCode == 100) {
                val form_head = data!!.getStringExtra("form_head")
                val body = data!!.getStringExtra("body")
                adapter!!.addArticle(Article(0, form_head!!, body!!, 0))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onResume() {
        super.onResume()
        binding!!.addArticleBtn.startAnimation(
            AnimationUtils.loadAnimation(
                requireActivity(),
                R.anim.fade_inandout
            )
        )
    }
}