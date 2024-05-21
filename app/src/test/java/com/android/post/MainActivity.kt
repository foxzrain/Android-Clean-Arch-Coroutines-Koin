package com.android.post

import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.post.data.model.ArticleEntity
import com.android.post.ui.main.MainActivity
import com.android.post.ui.main.MainViewModel
import io.mockk.every
import io.mockk.mockk
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
class MainActivityTest : KoinTest {
    private val viewModel: MainViewModel = mockk(relaxed = true)

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        val testModule = module {
            viewModel { viewModel }
        }
        modules(testModule)
    }

    @Before
    fun setUp() {
        // Mock ViewModel methods if necessary
        val articlesLiveData = MutableLiveData<List<ArticleEntity>>()
        every { viewModel.articles } returns articlesLiveData

        val isLoadingLiveData = MutableLiveData<Boolean>()
        every { viewModel.isLoading } returns isLoadingLiveData
    }

    @Test
    fun testActivityLaunchesAndDisplaysArticles() {
        val articles = listOf(
            ArticleEntity("link1", "Title 1", "Date 1", "Content 1"),
            ArticleEntity("link2", "Title 2", "Date 2", "Content 2")
        )

        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                val recyclerView = activity.findViewById<RecyclerView>(R.id.recyclerView)
                assertEquals(0, recyclerView.adapter?.itemCount)

                val articlesLiveData = viewModel.articles as MutableLiveData
                articlesLiveData.postValue(articles)

                assertEquals(2, recyclerView.adapter?.itemCount)
            }
        }
    }

    @Test
    fun testLoadingState() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                val progressBar = activity.findViewById<ProgressBar>(R.id.progressBar)

                val isLoadingLiveData = viewModel.isLoading as MutableLiveData
                isLoadingLiveData.postValue(true)
                assertEquals(View.VISIBLE, progressBar.visibility)

                isLoadingLiveData.postValue(false)
                assertEquals(View.GONE, progressBar.visibility)
            }
        }
    }
}
