package com.example.news.interfaces

import com.example.news.model.NewsApiModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("v2/top-headlines")
    fun getCategoryNews(
        @Query("category") category: String?,
        @Query("country") country: String?,
        @Query("apiKey") apiKey: String="9ffdc71e43f448b3b8dfd4114ed9df03"
    ): Call<NewsApiModel>
    @GET("v2/everything")
    fun getCarousalNews(
        @Query("q") q: String?,
        @Query("apiKey") apiKey: String="9ffdc71e43f448b3b8dfd4114ed9df03"
        ): Call<NewsApiModel>

    @GET("v2/everything")
    fun getSearchNews(
        @Query("q") q: String?,
        @Query("apiKey") apiKey: String="9ffdc71e43f448b3b8dfd4114ed9df03"
    ): Call<NewsApiModel>

    @GET("/v2/top-headlines")
    fun getChannelNews(
        @Query("sources") sources: String?,
        @Query("apiKey") apiKey: String="9ffdc71e43f448b3b8dfd4114ed9df03"
    ) : Call<NewsApiModel>
}