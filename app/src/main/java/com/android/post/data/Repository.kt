package com.android.post.data

import androidx.lifecycle.LiveData
import com.android.post.data.local.ArticleDao
import com.android.post.data.local.ArticleEntity
import com.android.post.data.remote.RemoteDataSource


class Repository(
    private val remoteDataSource: RemoteDataSource,
    private val articleDao: ArticleDao
) {
    val articles: LiveData<List<ArticleEntity>> = articleDao.getAllArticles()

    suspend fun refreshArticles() {
        val response = remoteDataSource.getArticles()
        if (response.isSuccessful) {
            response.body()?.let {
                val articles = it.toEntityList()
                articleDao.insertAll(articles)
            }
        }
    }
}