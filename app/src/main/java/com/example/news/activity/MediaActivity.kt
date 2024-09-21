package com.example.news.activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.news.R
import com.example.news.adapter.NewsApiAdapter
import com.example.news.databinding.ActivityMediaBinding
import com.example.news.domain.ApiClient.Companion.getApi
import com.example.news.domain.ApplicationNetwork
import com.example.news.interfaces.ApiInterface
import com.example.news.model.ArticlesItem
import com.example.news.model.NewsApiModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("UNCHECKED_CAST")
class MediaActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMediaBinding
    private var mediaList = mutableListOf<ArticlesItem>()
    private lateinit var newsAdapter : NewsApiAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        (application as ApplicationNetwork).liveData.observe(this@MediaActivity){
            if(it){
                binding.nestedMedia.visibility = View.VISIBLE
                binding.imgMediaNoNet.visibility = View.GONE
                binding.txtNoMediaNet.visibility = View.GONE
                binding.txtMediaNoNet2.visibility = View.GONE
            }
            else{
                binding.nestedMedia.visibility = View.GONE
                binding.imgMediaNoNet.visibility = View.VISIBLE
                binding.txtNoMediaNet.visibility = View.VISIBLE
                binding.txtMediaNoNet2.visibility = View.VISIBLE
            }
        }
        initClick()
    }
    private fun initClick(){
        binding.cvCNN.setOnClickListener {
            mediaApi("cnn")
        }
        binding.cvESPN.setOnClickListener {
            mediaApi("espn")
        }
        binding.cvBBC.setOnClickListener {
            mediaApi("bbc-news")
        }
        binding.cvNBC.setOnClickListener {
            mediaApi("nbc-news")
        }
        binding.imgMediaBack.setOnClickListener {
            finish()
        }
    }
    private fun mediaApi(selectedSource:String){
        val retrofit=getApi().create(ApiInterface::class.java)
        retrofit.getChannelNews(sources = selectedSource).enqueue(object : Callback<NewsApiModel> {
            override fun onResponse(call: Call<NewsApiModel>, response: Response<NewsApiModel>) {
                if(response.isSuccessful){
                    mediaList=response.body()!!.articles as MutableList<ArticlesItem>
                    setAdapter()
                }
            }
            override fun onFailure(call: Call<NewsApiModel>, t: Throwable) {
            }

        })
    }
    private fun setAdapter(){
        newsAdapter = NewsApiAdapter(mediaList)
        binding.rvMedia.adapter = newsAdapter
    }
}