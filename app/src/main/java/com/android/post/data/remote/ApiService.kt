package com.android.post.data.remote

import com.android.post.data.model.ArticleEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("feed/@primoapp")
    suspend fun getArticles(): List<ArticleEntity>

}