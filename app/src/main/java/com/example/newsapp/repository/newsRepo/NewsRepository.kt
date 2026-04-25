package com.example.newsapp.repository.newsRepo

import android.util.Log
import com.example.newsapp.data.api.NewsAPI
import com.example.newsapp.data.api.NewsResult
import javax.inject.Inject

class NewsRepository @Inject constructor (){

    suspend fun getArticles(): NewsResult? {
        return try {
            val response = NewsAPI.NewsApi.service.getArticles()
            Log.d("NewsRepository", "Code: ${response.code()}")
            Log.d("NewsRepository", "Body: ${response.body()}")
            Log.d("NewsRepository", "Error: ${response.errorBody()?.string()}")
            if (response.isSuccessful) {
                response.body()?.articles
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("NewsRepository", "Exception: ${e.message}")
            null
        }
    }
}