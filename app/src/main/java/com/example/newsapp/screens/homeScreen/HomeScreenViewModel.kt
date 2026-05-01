package com.example.newsapp.screens.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.api.Article
import com.example.newsapp.data.firebase.FirestoreUser
import com.example.newsapp.repository.firebaseRepo.FirebaseRepository
import com.example.newsapp.repository.newsRepo.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val firebaseRepo: FirebaseRepository,
    private val newsRepo: NewsRepository
) : ViewModel() {

    private val _user = MutableStateFlow<FirestoreUser?>(null)
    val user: StateFlow<FirestoreUser?> = _user

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles



    init {
        loadUser()
        loadArticles()
    }

    private fun loadUser() {
        viewModelScope.launch {
            _user.value = firebaseRepo.getUser()
        }
    }

    private fun loadArticles() {
        viewModelScope.launch {
            val result = newsRepo.getArticles()
            _articles.value = result?.results ?: emptyList()
        }
    }
}