package com.android.post.domain.model

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class XmlFeed(
    val channel: Channel
)

@JsonClass(generateAdapter = true)
data class Channel(
    val title: String,
    val link: String,
    val items: List<Article>
)

@JsonClass(generateAdapter = true)
data class Article(
    val title: String,
    val link: String,
    val pubDate: String,
    val content: String
)