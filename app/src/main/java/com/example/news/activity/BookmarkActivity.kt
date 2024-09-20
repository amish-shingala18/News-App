package com.example.news.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.news.R
import com.example.news.adapter.NewsAdapter
import com.example.news.databinding.ActivityBookmarkBinding
import com.example.news.helper.DbRoomHelper.Companion.db
import com.example.news.model.ArticlesTable

class BookmarkActivity : AppCompatActivity() {
    private lateinit var binding : ActivityBookmarkBinding
    private var bookmarkList = mutableListOf<ArticlesTable>()
    private lateinit var newsAdapter : NewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setAdapter()
    }
    private fun setAdapter(){
        newsAdapter = NewsAdapter(bookmarkList)
        binding.rvBookmark.adapter = newsAdapter

    }
    override fun onResume() {
        if (db!=null){
            bookmarkList = db!!.dao().readBookmark()
            newsAdapter.dataSetChanged(bookmarkList)
            Log.d("TAG", "onResume: ${bookmarkList.size}")
        }
        super.onResume()
    }
}