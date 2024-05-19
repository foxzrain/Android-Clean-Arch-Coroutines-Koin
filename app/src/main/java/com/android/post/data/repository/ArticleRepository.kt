package com.android.post.data.repository

import androidx.lifecycle.LiveData
import com.android.post.data.database.ArticleDao
import com.android.post.data.database.ArticleEntity
import com.android.post.data.source.remote.ApiService

class ArticleRepository(private val articleDao: ArticleDao, private val apiService: ApiService) {
    val allArticles: LiveData<List<ArticleEntity>> = articleDao.getAll()

    suspend fun refreshArticles() {
        val feed = apiService.getFeed()
        val articles = feed.channel.items.map {
            ArticleEntity(it.link, it.title, it.pubDate, it.content)
        }
        articleDao.insertAll(*articles.toTypedArray())
    }
}