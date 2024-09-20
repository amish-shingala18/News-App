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
import com.example.news.model.ArticlesItem

class NewsApiAdapter(private var list :MutableList<ArticlesItem>) : RecyclerView.Adapter<NewsApiAdapter.NewsApiViewHolder>() {
    class NewsApiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = MainNewsSampleBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsApiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_news_sample,parent,false)
        return NewsApiViewHolder(view)
    }
    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: NewsApiViewHolder, position: Int) {
        Glide.with(holder.itemView.context).load(list[position].urlToImage).into(holder.binding.imgMainNews)
        holder.binding.mainNewsTitle.text = list[position].title
        holder.binding.mainNewsDate.text = list[position].publishedAt
        holder.binding.mainAuthor.text=list[position].author
        holder.binding.clMainNews.setOnClickListener {
            val intent = Intent(holder.itemView.context, NewsDetailActivity::class.java)
            intent.putExtra("newsTitle",list[position].title)
            intent.putExtra("newsDescription",list[position].description)
            intent.putExtra("newsImage",list[position].urlToImage)
            intent.putExtra("newsDate",list[position].publishedAt)
            intent.putExtra("newsAuthor",list[position].author)
            holder.itemView.context.startActivity(intent)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(l1: MutableList<ArticlesItem>){
        list.addAll(l1)
        notifyDataSetChanged()
    }
    fun search() {
        list.clear()
    }
//    @SuppressLint("NotifyDataSetChanged")
//    fun dataSetChanged(l1:MutableList<ArticlesItem>){
//        list=l1
//        notifyDataSetChanged()
//    }
}