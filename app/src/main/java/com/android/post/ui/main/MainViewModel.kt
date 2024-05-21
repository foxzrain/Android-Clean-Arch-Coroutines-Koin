package com.android.post.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.post.data.ArticleRepository
import com.android.post.data.model.ArticleEntity
import com.android.post.domain.exception.traceErrorException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: ArticleRepository) : ViewModel() {
    private val _articles = MutableLiveData<List<ArticleEntity>>()
    val articles: LiveData<List<ArticleEntity>> = _articles

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        loadArticles()
    }

    private fun loadArticles() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.articles.collect {
                _articles.value = it
            }
            _isLoading.value = false
        }
    }

    fun refreshArticles() {
        viewModelScope.launch {
            try {
                val fetchedArticles = repository.fetchArticles()
                _articles.postValue(fetchedArticles)
            } catch (e: Exception) {
                handleApiError(e)
            }
        }
    }

    private fun handleApiError(exception: Exception) {
        val apiError = traceErrorException(exception)
        _error.value = apiError.getErrorMessage()
    }
}
