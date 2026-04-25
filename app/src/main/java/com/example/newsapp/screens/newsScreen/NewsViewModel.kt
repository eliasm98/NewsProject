package com.example.newsapp.screens.newsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.api.Article
import com.example.newsapp.data.datastore.NewsDatastore
import com.example.newsapp.repository.newsRepo.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsDataStore: NewsDatastore,
    private val repository: NewsRepository
) : ViewModel() {

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        viewModelScope.launch {
            newsDataStore.getArticles().collect { cached ->
                if (cached != null) {
                    _articles.value = cached.results
                } else {
                    refresh()
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.update { true }
            val result = repository.getArticles()
            result?.let {
                newsDataStore.saveArticles(it)
                _articles.value = it.results
            }
            _isRefreshing.update { false }
        }
    }
}