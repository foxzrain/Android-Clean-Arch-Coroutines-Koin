package com.android.post

import android.content.Context
import androidx.lifecycle.Observer
import androidx.room.Room
import com.android.post.data.ArticleRepository
import com.android.post.data.model.AppDatabase
import com.android.post.data.model.ArticleDao
import com.android.post.data.model.ArticleEntity
import com.android.post.data.remote.ApiService
import com.android.post.di.appDatabase
import com.android.post.ui.main.MainViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.just
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@ExperimentalCoroutinesApi
class KoinModuleTest : KoinTest {
    private val viewModel: MainViewModel = mockk<MainViewModel>()
    private var articleDao: ArticleDao = mockk<ArticleDao>()
    private val apiService: ApiService = mockk<ApiService>()
    private val repository: ArticleRepository = mockk<ArticleRepository>()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        // Mocking Context
        val context = mockk<Context>(relaxed = true)

        // Create Retrofit instance
        val apiService: ApiService = Retrofit.Builder()
            .baseUrl("https://medium.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        // Create Room database
        val appDatabase: AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "app.db")
                .fallbackToDestructiveMigration()
                .build()

        val testModule = module {
            single { context } // Provide the mocked Context
            single { apiService }
            single { appDatabase }
            single { articleDao }
            single { repository }
            viewModel { viewModel }
        }

        modules(testModule)
    }

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        // Provide the mocked dispatcher to the ViewModelScope
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        // Reset the main dispatcher after the test
        Dispatchers.resetMain()
    }

    @Test
    fun `loadArticles should update articles`() = runTest {
        val articles = listOf(
            ArticleEntity("link1", "Title 1", "Date 1", "Content 1"),
            ArticleEntity("link2", "Title 2", "Date 2", "Content 2")
        )
        coEvery { articleDao.getAllArticles() } returns flowOf(articles)

        viewModel.loadArticles()
        advanceUntilIdle()

        assertEquals(articles, viewModel.articles.value)
    }

//    @Test
//    fun `refreshArticles should update articles`() = runTest {
//        var articles = listOf(
//            ArticleEntity("link1", "Title 1", "Date 1", "Content 1"),
//            ArticleEntity("link2", "Title 2", "Date 2", "Content 2")
//        )
//        coEvery { apiService.getArticles() } returns articles
//
//        articles = listOf(
//            ArticleEntity("link3", "Title 1", "Date 1", "Content 1"),
//            ArticleEntity("link4", "Title 2", "Date 2", "Content 2")
//        )
//        coEvery { articleDao.insertArticles(articles) } just Runs
//
//        viewModel.refreshArticles()
//        advanceUntilIdle()
//
//        assertEquals(articles, viewModel.articles.value)
//    }

    @Test
    fun `handleApiError should update error`() = runTest {
        val errorMessage = "An error occurred"
        coEvery { apiService.getArticles() } throws Exception(errorMessage)

        viewModel.refreshArticles()
        advanceUntilIdle()

        assertEquals(errorMessage, viewModel.error.value)
    }

    @Test
    fun `isLoading should be true during loadArticles and false after`() = runTest {
        val articles = listOf(
            ArticleEntity("link1", "Title 1", "Date 1", "Content 1"),
            ArticleEntity("link2", "Title 2", "Date 2", "Content 2")
        )
        coEvery { articleDao.getAllArticles() } returns flowOf(articles)

        val loadingObserver = mockk<Observer<Boolean>>(relaxed = true)
        viewModel.isLoading.observeForever(loadingObserver)

        viewModel.loadArticles()
        advanceUntilIdle()

        coVerifySequence {
            loadingObserver.onChanged(true)
            loadingObserver.onChanged(false)
        }

        viewModel.isLoading.removeObserver(loadingObserver)
    }
}
