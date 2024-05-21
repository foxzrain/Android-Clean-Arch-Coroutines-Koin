package com.android.post

import com.android.post.data.ArticleRepository
import com.android.post.data.model.ArticleDao
import com.android.post.data.remote.ApiService
import com.android.post.ui.main.MainViewModel
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.koin.core.component.inject
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.KoinTestRule

class KoinModuleTest : AutoCloseKoinTest() {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(testModule)
    }

    private val viewModel: MainViewModel by inject()

    @Test
    fun `test ViewModel injection`() {
        // Verify ViewModel is injected successfully
    }

    companion object {
        val testModule: Module = module {
            // Define test dependencies here
            single { MainViewModel(get()) }
            single { mockk<ArticleDao>() }
            single { mockk<ApiService>() }
            single<ArticleRepository> { ArticleRepository(get(), get()) }
        }
    }
}
