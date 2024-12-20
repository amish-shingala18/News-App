package com.example.news.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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