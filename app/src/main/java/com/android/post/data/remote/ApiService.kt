package com.android.post.data.remote

import com.android.post.data.model.ArticleEntity
import retrofit2.http.GET

interface ApiService {
    @GET("feed/@primoapp")
    suspend fun getArticles(): List<ArticleEntity>
}