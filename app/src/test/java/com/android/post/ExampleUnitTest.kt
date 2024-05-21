package com.android.post

import androidx.lifecycle.MutableLiveData
import com.android.post.data.Repository
import com.android.post.data.local.ArticleEntity
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest : KoinTest {
    private val repository: Repository = mockk()
    private val viewModel = MainViewModel(repository)

    @Test
    fun `fetchArticles updates articles LiveData`() = runBlockingTest {
        val articles = listOf(ArticleEntity("1", "Title", "Content", "Date"))
        coEvery { repository.refreshArticles() } just Runs
        coEvery { repository.articles } returns MutableLiveData(articles)

        viewModel.fetchArticles()

        assertEquals(articles, viewModel.articles.getOrAwaitValue())
    }
}
