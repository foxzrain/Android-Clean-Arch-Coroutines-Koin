package com.android.post.data.source.remote

import com.android.post.domain.model.XmlFeed
import retrofit2.http.GET

interface ApiService {
    @GET("feed/@primoapp")
    suspend fun getFeed(): XmlFeed
}