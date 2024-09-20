package com.example.news

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.news.activity.BookmarkActivity
import com.example.news.activity.MediaActivity
import com.example.news.activity.SearchActivity
import com.example.news.adapter.NewsApiAdapter
import com.example.news.databinding.ActivityMainBinding
import com.example.news.domain.ApiClient.Companion.getApi
import com.example.news.interfaces.ApiInterface
import com.example.news.model.ArticlesItem
import com.example.news.model.NewsApiModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("UNCHECKED_CAST")
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var imageList = ArrayList<SlideModel>()
    private var newsList = mutableListOf<ArticlesItem>()
    private lateinit var newsApiAdapter : NewsApiAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setUpImageSlider()
        bottomNavigationBar()
        getCountryApi("general")
        initClick()
    }
    private fun getCountryApi(categories:String){
        val retrofit=getApi().create(ApiInterface::class.java)
        retrofit.getCategoryNews(category = categories, country = "us").enqueue(object : Callback<NewsApiModel> {
            override fun onResponse(call: Call<NewsApiModel>, response: Response<NewsApiModel>) {
                if (response.isSuccessful){
                    if(response.body()!!.articles!!.isEmpty()){
                        binding.menu.visibility=View.VISIBLE
                        binding.imgMainNoResult.visibility=View.VISIBLE
                        binding.rvNews.visibility=View.GONE
                        binding.imgSlider.visibility=View.GONE
                        binding.textView2.visibility=View.GONE
                        binding.textView3.visibility=View.GONE
                        binding.cartView.visibility=View.GONE
                    }else {
                        binding.imgMainNoResult.visibility=View.GONE
                        newsList = response.body()!!.articles as MutableList<ArticlesItem>
                        newsApiAdapter = NewsApiAdapter(newsList)
                        binding.rvNews.adapter = newsApiAdapter
                        Log.d("TAG", "onResponse: ${response.body()}")
                    }
                }
            }
            override fun onFailure(call: Call<NewsApiModel>, t: Throwable) {}
        })
    }
    private fun setUpImageSlider() {
        val retrofit = getApi().create(ApiInterface::class.java)
        retrofit.getCarousalNews(q = "everything").enqueue(object : Callback<NewsApiModel> {
            override fun onResponse(call: Call<NewsApiModel>, response: Response<NewsApiModel>) {
                if (response.isSuccessful) {
                    val articles = response.body()?.articles
                    articles?.let { articlesList ->
                        imageList = ArrayList()
                        for (x in articlesList) {
                            if (!x!!.urlToImage.isNullOrEmpty()) {
                                imageList.add(SlideModel(x.urlToImage, x.title ?: ""))
                            }
                        }
                        if (imageList.isNotEmpty()) {
                            binding.imgSlider.setImageList(imageList, ScaleTypes.FIT)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<NewsApiModel>, t: Throwable) {}
        })
    }
    private fun bottomNavigationBar(){
        binding.menu.setItemSelected(R.id.home,true)
        binding.menu.setOnItemSelectedListener {
            when(it){
                R.id.home ->{
                    binding.menu.setItemSelected(R.id.home,true)
                }
                R.id.search ->{
                    binding.menu.setItemSelected(R.id.search,true)
                    startActivity(Intent(this, SearchActivity::class.java))
                }
                R.id.media -> {
                    binding.menu.setItemSelected(R.id.media, true)
                    startActivity(Intent(this, MediaActivity::class.java))
                }
                R.id.bookmark -> {

                    binding.menu.setItemSelected(R.id.bookmark, true)
                    startActivity(Intent(this, BookmarkActivity::class.java))
                }
                else -> {
                    binding.menu.setItemSelected(R.id.home,true)
                }
            }
        }
    }
    private fun initClick(){
        binding.cvGeneralCat.setOnClickListener {
            getCountryApi("general")
        }
        binding.cvEntertainmentCat.setOnClickListener {
            getCountryApi("entertainment")
        }
        binding.cvBusinessCat.setOnClickListener {
            getCountryApi("business")
        }
        binding.cvHealthCat.setOnClickListener {
            getCountryApi("health")
        }
        binding.cvSportsCat.setOnClickListener {
            getCountryApi("sports")
        }
    }
    override fun onResume() {
        binding.menu.setItemSelected(R.id.home,true)
        super.onResume()
    }
}