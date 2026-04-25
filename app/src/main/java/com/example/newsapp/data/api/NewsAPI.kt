package com.example.newsapp.data.api

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class NewsAPI {

    data class NewsResponse(
        val articles: NewsResult
    )

    interface NewsApiService {
        @GET("api/v1/article/getArticles")
        suspend fun getArticles(
            @Query("apiKey") apiKey: String = "c155dec5-f79e-4ff1-8ed6-a51669a97cba",
            @Query("lang") lang: String = "eng",
            @Query("articlesCount") count: Int = 100,
            @Query("resultType") resultType: String = "articles",
            @Query( "includeArticleImage") includeImage: Boolean = true
        ): Response<NewsResponse>
    }

    object NewsApi {
        val service: NewsApiService by lazy {
            Retrofit.Builder()
                .baseUrl("https://eventregistry.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NewsApiService::class.java)
        }
    }
}