package com.android.post.data

import com.android.post.data.local.ArticleDao
import com.android.post.data.local.ArticleEntity
import com.android.post.data.remote.ApiService
import kotlinx.coroutines.flow.Flow

class ArticleRepository(
    private val articleDao: ArticleDao,
    private val apiService: ApiService
) {
    val articles: Flow<List<ArticleEntity>> = articleDao.getAllArticles()

    suspend fun refreshArticles() {
        val articles = apiService.getArticles()
        articleDao.insertArticles(articles.map {
            ArticleEntity(it.link, it.title, it.pubDate, it.content)
        })
    }
}
