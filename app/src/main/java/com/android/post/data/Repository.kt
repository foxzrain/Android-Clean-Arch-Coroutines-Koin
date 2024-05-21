package com.android.post.data

import com.android.post.data.model.ArticleDao
import com.android.post.data.model.ArticleEntity
import com.android.post.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ArticleRepository(
    private val articleDao: ArticleDao,
    private val apiService: ApiService
) {
    val articles: Flow<List<ArticleEntity>> = articleDao.getAllArticles()

    suspend fun fetchArticles(): List<ArticleEntity> {
        return withContext(Dispatchers.IO) {
            val articles = apiService.getArticles()
            articleDao.insertArticles(articles)
            return@withContext articles
        }
    }

    suspend fun deleteAllArticles() {
        return withContext(Dispatchers.IO) {
            return@withContext articleDao.deleteAllArticles()
        }
    }
}

