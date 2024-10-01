package com.oscorella.cafeyn.interests.data

import com.oscorella.cafeyn.core.db.TopicDao
import com.oscorella.cafeyn.core.db.mapper.asDomain
import com.oscorella.cafeyn.core.db.mapper.asEntity
import com.oscorella.cafeyn.core.network.Result
import com.oscorella.cafeyn.interests.domain.Topic
import javax.inject.Inject

interface TopicRepository {

    suspend fun getAllTopics(): Result<List<Topic>>

    suspend fun getFavoriteTopics(): List<Topic>

    suspend fun saveFavoriteTopics(topics: List<Topic>)

}

class TopicRepositoryImpl @Inject constructor(
    private val topicService: TopicService,
    private val topicDao: TopicDao
) : TopicRepository {

    override suspend fun getAllTopics(): Result<List<Topic>> {
        val response = topicService.getTopics()
        return if(response.isSuccessful) {
            response.body()?.let { data ->
                // Flatmap topics and subtopics into a single list
                val flattenedList = data.flatMap { topic -> mutableListOf(topic).also { it.addAll(topic.subTopics) }  }
                // Add index to topics to be able to sort them inside a TreeSet
                flattenedList.forEachIndexed { index, topic ->
                    topic.index = index
                }
                Result.Success(flattenedList)
            } ?: run {
                 // We assume that if the response is null, there is an error.
                Result.Error(response.message(), response.code())
            }
        }
        else {
            Result.Error(response.message(), response.code())
        }
    }

    override suspend fun getFavoriteTopics(): List<Topic>  {
        return topicDao.getAllFavoriteTopics().asDomain()
    }

    override suspend fun saveFavoriteTopics(topics: List<Topic>) {
        topicDao.deleteFavoriteTopics()
        topicDao.insertFavoriteTopicList(topics.asEntity())
    }

}