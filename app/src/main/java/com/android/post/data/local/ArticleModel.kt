package com.android.post.data.local


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArticleModel(
    @Json(name = "title") val title: String,
    @Json(name = "link") val link: String,
    @Json(name = "pubDate") val pubDate: String,
    @Json(name = "content:encoded") val content: String
)
