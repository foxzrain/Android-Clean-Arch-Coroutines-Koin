package com.android.post.data.remote

import com.android.post.domain.model.DataXmlFeed
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("feed/@primoapp")
    suspend fun getArticles(): Response<DataXmlFeed>
}