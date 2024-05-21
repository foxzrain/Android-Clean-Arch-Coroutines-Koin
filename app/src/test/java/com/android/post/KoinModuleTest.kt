package com.android.post

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.post.data.ArticleRepository
import com.android.post.data.model.AppDatabase
import com.android.post.data.model.ArticleDao
import com.android.post.data.model.ArticleEntity
import com.android.post.data.remote.ApiService
import com.android.post.ui.main.MainViewModel
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.test.assertEquals
import org.koin.androidx.viewmodel.dsl.viewModel

//@RunWith(AndroidJUnit4::class)
class KoinModuleTest : KoinTest {
    private val viewModel: MainViewModel = mockk()
    private val articleDao: ArticleDao = mockk()
    private val apiService: ApiService = mockk()
    private val repository: ArticleRepository = mockk()

    @get:Rule
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        // Mocking Context
        val context = mockk<Context>(relaxed = true)

        val httpInterceptor = HttpLoggingInterceptor()
        httpInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        // Create Retrofit instance
        val apiService: ApiService = Retrofit.Builder().client(
            OkHttpClient.Builder()
                .addInterceptor(httpInterceptor).build()
        )
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://medium.com/")
            .build()
            .create(ApiService::class.java)

        // Create Room database
        val appDatabase: AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "app.db")
                .fallbackToDestructiveMigration()
                .build()

        val testModule = module {
            single { context }
            single { apiService }
            single { appDatabase }
            single { articleDao }
            single { repository }
            viewModel { viewModel }
        }

        startKoin {
            modules(testModule)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `refreshArticles should update articles`() = runTest {
        val articles = listOf(
            ArticleEntity("link1", "Title 1", "Date 1", "Content 1"),
            ArticleEntity("link2", "Title 2", "Date 2", "Content 2")
        )
        coEvery { repository.fetchArticles() } returns articles
        coEvery { articleDao.getAllArticles() } returns flowOf(articles)

        viewModel.refreshArticles()
        advanceUntilIdle()

        assertEquals(articles, viewModel.articles.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `handleApiError should update error`() = runTest {
        val errorMessage = "An error occurred"
        coEvery { repository.fetchArticles() } throws Exception(errorMessage)

        viewModel.refreshArticles()
        advanceUntilIdle()

        assertEquals(errorMessage, viewModel.error.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `isLoading should be true during refreshArticles and false after`() = runTest {
        val articles = listOf(
            ArticleEntity("link1", "Title 1", "Date 1", "Content 1"),
            ArticleEntity("link2", "Title 2", "Date 2", "Content 2")
        )
        coEvery { repository.fetchArticles() } returns articles
        coEvery { articleDao.getAllArticles() } returns flowOf(articles)

        val loadingObserver = mockk<Observer<Boolean>>(relaxed = true)
        viewModel.isLoading.observeForever(loadingObserver)

        viewModel.refreshArticles()
        advanceUntilIdle()

        coVerifySequence {
            loadingObserver.onChanged(true)
            loadingObserver.onChanged(false)
        }

        viewModel.isLoading.removeObserver(loadingObserver)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}