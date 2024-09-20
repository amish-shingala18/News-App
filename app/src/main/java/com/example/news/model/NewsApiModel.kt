package com.example.news.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity("NewsTable")
data class NewsApiModel(
	val totalResults: Int? = null,
	val articles: List<ArticlesItem?>? = null,
	val status: String? = null
)

data class Source(
	val name: String? = null,
	val id: String? = null
)

data class ArticlesItem(
	val publishedAt: String? = null,
	val author: String? = null,
	val urlToImage: String? = null,
	val description: String? = null,
	val source: Source? = null,
	val title: String? = null,
	val url: String? = null,
	val content: String? = null
)

@Entity(tableName = "NewsTable")
data class ArticlesTable(
	@PrimaryKey(autoGenerate = true)
	var id :Int = 0,
	@ColumnInfo(name = "publishedAt")
	val publishedAt: String? = null,
	@ColumnInfo(name = "author")
	val author: String? = null,
	@ColumnInfo(name = "urlToImage")
		val urlToImage: String? = null,
	@ColumnInfo(name = "description")
	val description: String? = null,
	@ColumnInfo(name = "title")
	val title: String? = null,
	@ColumnInfo(name = "url")
	val url: String? = null,
	@ColumnInfo(name = "content")
	val content: String? = null
)