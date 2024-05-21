package com.android.post.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.post.data.ArticleRepository
import com.android.post.data.local.ArticleEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: ArticleRepository) : ViewModel() {
    private val _articles = MutableStateFlow<List<ArticleEntity>>(emptyList())
    val articles: StateFlow<List<ArticleEntity>> = _articles

    init {
        loadArticles()
    }

    private fun loadArticles() {
        viewModelScope.launch {
            repository.articles.collect {
                _articles.value = it
            }
        }
    }

    fun refreshArticles() {
        viewModelScope.launch {
            repository.refreshArticles()
        }
    }
}
