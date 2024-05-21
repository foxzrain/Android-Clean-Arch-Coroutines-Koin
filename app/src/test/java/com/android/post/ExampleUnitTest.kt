import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.post.data.ArticleRepository
import com.android.post.ui.main.MainViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals


@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel
    private lateinit var repository: ArticleRepository

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        repository = mockk()
        viewModel = MainViewModel(repository)
    }

    @Test
    fun `loadData - success`() = runBlockingTest {
        // Mock repository response
        val mockArticles = listOf(
            Article(1, "Title 1", "Link 1", emptyList(), "Author 1", "2024-05-20", "Content 1"),
            Article(2, "Title 2", "Link 2", emptyList(), "Author 2", "2024-05-19", "Content 2")
        )
        coEvery { repository.fetchArticles() } returns emptyList()
        coEvery { repository.articles } returns mockArticles

        // Call method under test
        viewModel.refreshArticles()

        // Assert loading state
        //assertEquals(true, viewModel)

        // Advance the coroutine dispatcher to execute the code inside viewModel.loadData()
        advanceUntilIdle()

        // Assert loading state is false
        assertEquals(false, viewModel.isLoading.value)

        // Assert articles are set correctly
        assertEquals(mockArticles, viewModel.articles.value)
    }

    // Add more test cases for error handling, etc.
}
