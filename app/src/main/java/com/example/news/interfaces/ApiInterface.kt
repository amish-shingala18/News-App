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
        @Query("apiKey") apiKey: String="78a9b796f1134770865f64bcdec4e2da"
    ): Call<NewsApiModel>


//    @GET("v2/top-headlines")
//    fun getCountryNews(
//        @Query("country") country: String?,
//        @Query("apiKey") apiKey: String="78a9b796f1134770865f64bcdec4e2da"
//    ): Call<NewsApiModel>

    @GET("v2/everything")
    fun getCarousalNews(
        @Query("q") q: String?,
        @Query("apiKey") apiKey: String="78a9b796f1134770865f64bcdec4e2da"
        ): Call<NewsApiModel>

    @GET("v2/everything")
    fun getSearchNews(
        @Query("q") q: String?,
        @Query("apiKey") apiKey: String="78a9b796f1134770865f64bcdec4e2da"
    ): Call<NewsApiModel>

    @GET("/v2/top-headlines")
    fun getChannelNews(
        @Query("sources") sources: String?,
        @Query("apiKey") apiKey: String="78a9b796f1134770865f64bcdec4e2da"
    ) : Call<NewsApiModel>
}