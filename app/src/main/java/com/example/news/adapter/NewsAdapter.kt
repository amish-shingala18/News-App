package com.example.news.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.activity.NewsDetailActivity
import com.example.news.databinding.MainNewsSampleBinding
import com.example.news.model.ArticlesTable

class NewsAdapter(private var list: List<ArticlesTable>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = MainNewsSampleBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_news_sample,parent,false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.binding.mainNewsTitle.text = list[position].title
        holder.binding.mainAuthor.text = list[position].author
        holder.binding.mainNewsDate.text = list[position].publishedAt
        Glide.with(holder.itemView.context).load(list[position].urlToImage).into(holder.binding.imgMainNews)
        holder.binding.clMainNews.setOnClickListener {
            val intent = Intent(holder.itemView.context, NewsDetailActivity::class.java)
            intent.putExtra("newsBookmarkTitle",list[position].title)
            intent.putExtra("newsBookmarkDescription",list[position].description)
            intent.putExtra("newsBookmarkImage",list[position].urlToImage)
            intent.putExtra("newsBookmarkAuthor",list[position].author)
            holder.itemView.context.startActivity(intent)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun dataSetChanged(l1:MutableList<ArticlesTable>){
        list=l1
        notifyDataSetChanged()
    }
}