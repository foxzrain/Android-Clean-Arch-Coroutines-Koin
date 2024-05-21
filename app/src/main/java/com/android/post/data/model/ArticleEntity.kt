package com.android.post.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val link: String,
    val title: String,
    val pubDate: String,
    val content: String
) : Parcelable

