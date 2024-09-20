package com.example.news.activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.databinding.ActivityNewsDetailBinding
import com.example.news.helper.DbRoomHelper.Companion.db
import com.example.news.helper.DbRoomHelper.Companion.initDb
import com.example.news.model.ArticlesTable

class NewsDetailActivity : AppCompatActivity() {

    private var newsAuthor: String?=null
    private var newsDate: String?=null
    private var newsImage: String?=null
    private var newsDescription: String?=null
    private var newsTitle: String?=null
    private lateinit var binding : ActivityNewsDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initDb(this@NewsDetailActivity)
        initCLick()
        getNewsData()
    }
    private fun initCLick(){
        binding.imgBookmark.setOnClickListener {
            binding.imgBookmark.setImageResource(R.drawable.selected_bookmark)
            val articlesTable = ArticlesTable(publishedAt = newsDate,author=newsAuthor,
                urlToImage=newsImage,description=newsDescription,title=newsTitle)
            db!!.dao().insertBookmark(articlesTable)
        }
    }
    private fun getNewsData(){
        newsTitle = intent.getStringExtra("newsTitle")
        newsDescription = intent.getStringExtra("newsDescription")
        newsImage = intent.getStringExtra("newsImage")
        newsAuthor = intent.getStringExtra("newsAuthor")
        newsDate = intent.getStringExtra("newsDate")
        binding.txtDetailNewsTitle.text = newsTitle
        binding.txtDetailNewsDescription.text = newsDescription
        Glide.with(this).load(newsImage).into(binding.imgDetailNews)
    }
}