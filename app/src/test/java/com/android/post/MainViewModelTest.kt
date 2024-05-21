package com.android.post

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.post.data.ArticleRepository
import com.android.post.data.model.ArticleEntity
import com.android.post.ui.main.MainViewModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import kotlin.test.assertEquals

//@RunWith(AndroidJUnit4::class)
class MainViewModelTest : KoinTest {
    private lateinit var viewModel: MainViewModel
    private val repository: ArticleRepository = mockk()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        val testModule = module {
            single { repository }
            viewModel { MainViewModel(repository) }
        }
        modules(testModule)
    }

    @Before
    fun setUp() {
        viewModel = MainViewModel(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `refreshArticles should update articles`() = runTest {
        val articles = listOf(
            ArticleEntity("link1", "Title 1", "Date 1", "Content 1"),
            ArticleEntity("link2", "Title 2", "Date 2", "Content 2")
        )
        coEvery { repository.fetchArticles() } returns articles

        viewModel.refreshArticles()
        advanceUntilIdle()

        assertEquals(articles, viewModel.articles.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loading state should be true during refresh and false after`() = runTest {
        val articles = listOf(
            ArticleEntity("link1", "Title 1", "Date 1", "Content 1"),
            ArticleEntity("link2", "Title 2", "Date 2", "Content 2")
        )
        coEvery { repository.fetchArticles() } returns articles

        val loadingObserver = mockk<Observer<Boolean>>(relaxed = true)
        viewModel.isLoading.observeForever(loadingObserver)

        viewModel.refreshArticles()
        advanceUntilIdle()

        verifySequence {
            loadingObserver.onChanged(true)
            loadingObserver.onChanged(false)
        }

        viewModel.isLoading.removeObserver(loadingObserver)
    }
}
