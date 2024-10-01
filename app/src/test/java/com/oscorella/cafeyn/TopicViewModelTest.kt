package com.oscorella.cafeyn

import com.oscorella.cafeyn.interests.data.TopicRepository
import com.oscorella.cafeyn.interests.presentation.InterestsViewModel
import com.oscorella.cafeyn.core.network.Result
import com.oscorella.cafeyn.interests.domain.Name
import com.oscorella.cafeyn.interests.domain.Topic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.Mockito

class TopicViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var topicRepository: TopicRepository
    private lateinit var viewModel: InterestsViewModel

    @Before
    fun setup() {
        topicRepository = Mockito.mock(TopicRepository::class.java)
        viewModel = InterestsViewModel(topicRepository)
    }

    @Test
    fun afterNetworkSuccessGetLocalTopics() = runTest {
        Mockito.doReturn(listOf(Topic("1", Name("News","key"), emptyList()))).`when`(topicRepository).getFavoriteTopics()

        viewModel.handleSuccessResult(mockTopicsResponse.data)

        Mockito.verify(topicRepository, Mockito.times(1)).getFavoriteTopics()
    }

    @Test
    fun topicListDoesNotContainFavoritesList() = runTest {
        Mockito.doReturn(listOf(mockTopicsResponse.data[0])).`when`(topicRepository).getFavoriteTopics()

        viewModel.handleSuccessResult(mockTopicsResponse.data)

        assert(mockTopicsResponse.data.size == 2)
        assert(viewModel.topicList.value.size == 1)
        assert(viewModel.favoritesList.value.size == 1)
    }

    private val mockTopicsResponse: Result.Success<List<Topic>>
        get() {
            return Result.Success(
                testTopics
            )
        }

    private val testTopics: List<Topic>
        get() {
            return listOf(
                Topic("1", Name("News","key"), emptyList(), 1),
                Topic("2", Name("Regions","key"), emptyList(), 2)
            )
        }

}

//TODO: Add to own file
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}