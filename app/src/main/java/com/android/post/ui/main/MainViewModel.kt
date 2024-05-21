package com.android.post.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.post.data.Repository
import com.android.post.data.local.ArticleEntity
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    val articles: LiveData<List<ArticleEntity>> = repository.articles
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        _isLoading.value = false
    }

    fun fetchArticles() {
        _isLoading.value = true
        viewModelScope.launch {
            repository.refreshArticles()
            _isLoading.value = false
        }
    }
}