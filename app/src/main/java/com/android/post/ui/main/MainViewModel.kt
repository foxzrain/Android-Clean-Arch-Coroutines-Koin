package com.android.post.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.android.post.data.ArticleRepository
import com.android.post.data.model.ArticleEntity
import com.android.post.domain.exception.traceErrorException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: ArticleRepository) : ViewModel() {
    private val _articles = MutableLiveData<List<ArticleEntity>>() // Private mutable LiveData
    val articles: LiveData<List<ArticleEntity>> = _articles // Public read-only LiveData

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loadArticles() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Collect articles from the repository
                repository.articles.collect { articles ->
                    // Update LiveData with the latest list of articles
                    _articles.value = articles
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshArticles() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Fetch articles from the network and update the database
                repository.fetchArticles()
            } catch (e: Exception) {
                handleApiError(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun handleApiError(exception: Exception) {
        val apiError = traceErrorException(exception)
        _error.value = apiError.getErrorMessage()
    }
}



