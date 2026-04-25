package com.example.newsapp.data.api

data class Article(
    val uri: String,
    val dateTime: String,
    val url: String,
    val title: String,
    val image: String?,
)
