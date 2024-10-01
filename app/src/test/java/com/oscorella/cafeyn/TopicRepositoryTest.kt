package com.oscorella.cafeyn

import com.oscorella.cafeyn.core.db.TopicDao
import com.oscorella.cafeyn.core.db.mapper.asEntity
import com.oscorella.cafeyn.core.network.Result
import com.oscorella.cafeyn.interests.data.TopicRepository
import com.oscorella.cafeyn.interests.data.TopicRepositoryImpl
import com.oscorella.cafeyn.interests.data.TopicService
import com.oscorella.cafeyn.interests.domain.Name
import com.oscorella.cafeyn.interests.domain.Topic
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import retrofit2.Response

class TopicRepositoryTest {

    private lateinit var topicService: TopicService
    private lateinit var topicRepository: TopicRepository
    private lateinit var topicDao: TopicDao

    private val listOfTopics = listOf(
        Topic("1", Name("News","key"), emptyList()),
        Topic("2", Name("Regions","key"), listOf(Topic("3", Name("Ile de france","key"), emptyList())))
    )

    @Before
    fun setup() {
        topicService = Mockito.mock(TopicService::class.java)
        topicDao = Mockito.mock(TopicDao::class.java)
        topicRepository = TopicRepositoryImpl(topicService, topicDao)
    }

    @Test
    fun getTopicsFromApiFlattensListAndSetsIndex() = runTest {
        Mockito.`when`(topicService.getTopics()).thenReturn(
            Response.success(listOfTopics)
        )
        val topics = topicRepository.getAllTopics()
        assert(topics is Result.Success)
        (topics as Result.Success)
        assert(topics.data.size == 3)
        assert(topics.data[0].index == 0)
        assert(topics.data[1].index == 1)
        assert(topics.data[2].index == 2)
    }

    @Test
    fun getFavTopicsFromDbCallsDaoMethod() = runTest {
        topicRepository.getFavoriteTopics()
        Mockito.verify(topicDao, Mockito.times(1)).getAllFavoriteTopics()
    }

    @Test
    fun saveFavoriteTopicsCallsDaoMethods() = runTest {
        topicRepository.saveFavoriteTopics(listOfTopics)
        Mockito.verify(topicDao, Mockito.times(1)).deleteFavoriteTopics()
        Mockito.verify(topicDao, Mockito.times(1)).insertFavoriteTopicList(listOfTopics.asEntity())
    }

}