package com.example.news.interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.news.model.ArticlesTable

@Dao
interface DAO {
    @Insert
    fun insertBookmark(articlesTable: ArticlesTable)
    @Delete
    fun deleteBookmark(articlesTable: ArticlesTable)
    @Query("SELECT * FROM NewsTable")
    fun readBookmark(): MutableList<ArticlesTable>
}