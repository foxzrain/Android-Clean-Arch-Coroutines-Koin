package com.android.post.data.remote

import com.android.post.data.local.ArticleEntity
import retrofit2.http.GET

interface ApiService {
    @GET("https://medium.com/feed/@primoapp")
    suspend fun getArticles(): List<ArticleEntity>
}