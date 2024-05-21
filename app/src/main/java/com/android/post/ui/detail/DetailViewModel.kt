package com.android.post.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.post.data.Repository
import com.android.post.data.local.ArticleEntity
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: Repository) : ViewModel() {
    private val _article = MutableLiveData<ArticleEntity>()
    val article: LiveData<ArticleEntity> get() = _article

    fun loadArticle(articleId: String) {
        viewModelScope.launch {
            _article.value = repository.getArticleById(articleId)
        }
    }
}