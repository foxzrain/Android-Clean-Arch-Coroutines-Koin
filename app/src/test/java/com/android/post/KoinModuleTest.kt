package com.android.post

import androidx.room.Room
import com.android.post.data.ArticleRepository
import com.android.post.data.model.AppDatabase
import com.android.post.data.model.ArticleDao
import com.android.post.data.model.ArticleEntity
import com.android.post.data.remote.ApiService
import com.android.post.ui.main.MainViewModel
import io.mockk.coEvery
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
import org.koin.core.component.inject
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.KoinTestRule
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class MainViewModelTest : AutoCloseKoinTest() {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(appModule)
    }

    private val articleDao: ArticleDao by inject()
    private val apiService: ApiService by inject()
    private val viewModel: MainViewModel by inject()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
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
//        val articles = listOf(
//            ArticleEntity("link1", "Title 1", "Date 1", "Content 1"),
//            ArticleEntity("link2", "Title 2", "Date 2", "Content 2")
//        )
//        coEvery { apiService.getArticles() } returns articles
//        coEvery { articleDao.insertArticles(any()) } just Runs
//
//        viewModel.refreshArticles()
//        advanceUntilIdle()
//
//        assertEquals(articles, viewModel.articles.value)
//    }
//
//    @Test
//    fun `handleApiError should update error`() = runTest {
//        val errorMessage = "An error occurred"
//        coEvery { apiService.getArticles() } throws Exception(errorMessage)
//
//        viewModel.refreshArticles()
//        advanceUntilIdle()
//
//        assertEquals(errorMessage, viewModel.error.value)
//    }
//
//    @Test
//    fun `isLoading should be true during loadArticles and false after`() = runTest {
//        val articles = listOf(
//            ArticleEntity("link1", "Title 1", "Date 1", "Content 1"),
//            ArticleEntity("link2", "Title 2", "Date 2", "Content 2")
//        )
//        coEvery { articleDao.getAllArticles() } returns flowOf(articles)
//
//        val loadingObserver = mockk<Observer<Boolean>>(relaxed = true)
//        viewModel.isLoading.observeForever(loadingObserver)
//
//        viewModel.loadArticles()
//        advanceUntilIdle()
//
//        coVerifySequence {
//            loadingObserver.onChanged(true)
//            loadingObserver.onChanged(false)
//        }
//
//        viewModel.isLoading.removeObserver(loadingObserver)
//    }

    companion object {
        val appModule = module {
            single {
                Retrofit.Builder().baseUrl("https://medium.com/")
                    .addConverterFactory(MoshiConverterFactory.create()).build()
                    .create(ApiService::class.java)
            }

            single {
                Room.databaseBuilder(get(), AppDatabase::class.java, "app.db")
                    .fallbackToDestructiveMigration().build()
            }

            single { get<AppDatabase>().articleDao() }

            single { ArticleRepository(get<AppDatabase>().articleDao(), get<ApiService>()) }

            viewModel { MainViewModel(get<ArticleRepository>()) }
        }
    }
}
