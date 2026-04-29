package com.example.newsapp.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.newsapp.data.api.NewsResult
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map



private val Context.dataStore by preferencesDataStore(name = "news_cache")

class NewsDatastore (private val context: Context) {
    companion object {
        private val NEWS_KEY = stringPreferencesKey("cached_articles")
    }
    // saves the whole NewsResults object (which already contains the list inside)
    suspend fun saveArticles(newsResult: NewsResult) {
        val json = Gson().toJson(newsResult)
        context.dataStore.edit { prefs ->
            prefs[NEWS_KEY] = json
        }
    }

    // reads it back and returns it as a Flow
    fun getArticles(): Flow<NewsResult?> {
        return context.dataStore.data.map { prefs ->
            val json = prefs[NEWS_KEY]
            if (json == null) {
                null                               // nothing cached yet
            } else {
                Gson().fromJson(json, NewsResult::class.java)
            }
        }
    }

}