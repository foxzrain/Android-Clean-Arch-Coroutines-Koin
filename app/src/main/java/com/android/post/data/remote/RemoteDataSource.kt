package com.android.post.data.remote

class RemoteDataSource(private val apiService: ApiService) {
    suspend fun getArticles() = apiService.getArticles()
}