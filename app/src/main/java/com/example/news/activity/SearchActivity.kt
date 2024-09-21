package com.example.news.activity

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.news.R
import com.example.news.adapter.NewsApiAdapter
import com.example.news.databinding.ActivitySearchBinding
import com.example.news.domain.ApiClient.Companion.getApi
import com.example.news.domain.ApplicationNetwork
import com.example.news.interfaces.ApiInterface
import com.example.news.model.ArticlesItem
import com.example.news.model.NewsApiModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("UNCHECKED_CAST")
class SearchActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySearchBinding
    private var newsSearchList = mutableListOf<ArticlesItem>()
    private lateinit var newsApiAdapter : NewsApiAdapter
    private var searchingNews="top headlines"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        (application as ApplicationNetwork).liveData.observe(this@SearchActivity){
            if(it){
                binding.nestedSearch.visibility = View.VISIBLE
                binding.imgSearchNoNet.visibility = View.GONE
                binding.txtNoSearchNet.visibility = View.GONE
                binding.txtSearchNoNet2.visibility = View.GONE
            }
            else{
                binding.nestedSearch.visibility = View.GONE
                binding.imgSearchNoNet.visibility = View.VISIBLE
                binding.txtNoSearchNet.visibility = View.VISIBLE
                binding.txtSearchNoNet2.visibility = View.VISIBLE
            }
        }
        setAdapter()
        searchNews()
        initClick()
    //searchApiNews(searchingNews)
    }
    private fun initClick(){
        binding.imgSearchBack.setOnClickListener {
            finish()
        }
    }
    private fun searchNews(){
        binding.svNews.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchingNews = query!!
                newsApiAdapter.search()
                searchApiNews(searchingNews)
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }
private fun searchApiNews(search: String){
        val retrofit = getApi().create(ApiInterface::class.java)
        retrofit.getSearchNews(q = search).enqueue(object :Callback<NewsApiModel>{
            override fun onResponse(call: Call<NewsApiModel>, response: Response<NewsApiModel>) {
                if (response.isSuccessful){
                    if(response.body()!!.articles!!.isEmpty()){
                        newsApiAdapter.updateList(response.body()!!.articles as MutableList<ArticlesItem>)
                        binding.imgSearchNoResult.visibility=View.VISIBLE
                        binding.txtNoResult.visibility=View.VISIBLE
                    } else {
                        newsApiAdapter.updateList(response.body()!!.articles as MutableList<ArticlesItem>)
                        newsSearchList = response.body()!!.articles as MutableList<ArticlesItem>
                        setAdapter()
                    }
                }
            }
            override fun onFailure(call: Call<NewsApiModel>, t: Throwable) {}
        })
    }
    private fun setAdapter(){
        newsApiAdapter = NewsApiAdapter(newsSearchList)
        binding.rvSearch.adapter = newsApiAdapter
    }
}