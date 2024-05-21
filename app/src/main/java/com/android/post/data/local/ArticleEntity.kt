package com.android.post.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val link: String,
    val title: String,
    val pubDate: String,
    val content: String
)
