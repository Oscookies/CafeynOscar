package com.oscorella.cafeyn.interests.data

import com.oscorella.cafeyn.core.db.TopicDao
import com.oscorella.cafeyn.core.db.mapper.asDomain
import com.oscorella.cafeyn.core.db.mapper.asEntity
import com.oscorella.cafeyn.core.di.CafeynDispatchers
import com.oscorella.cafeyn.core.di.Dispatcher
import com.oscorella.cafeyn.interests.domain.Topic
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

interface TopicRepository {

    suspend fun getAllTopics(
        onSuccess: (List<Topic>) -> Unit,
        onError: (String?) -> Unit,
    )

    suspend fun getFavoriteTopics(): List<Topic>

    suspend fun saveFavoriteTopics(topics: List<Topic>)

}

class TopicRepositoryImpl @Inject constructor(
    private val topicService: TopicService,
    private val topicDao: TopicDao,
    @Dispatcher(CafeynDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : TopicRepository {

    override suspend fun getAllTopics(
        onSuccess: (List<Topic>) -> Unit,
        onError: (String?) -> Unit,
    ) {
        val response = topicService.getTopics()
        response.onSuccess {
            onSuccess(this.data)
        }.onFailure {
            onError(this.message())
        }
    }

    override suspend fun getFavoriteTopics(): List<Topic>  {
        return topicDao.getAllFavoriteTopics().asDomain()
    }

    override suspend fun saveFavoriteTopics(topics: List<Topic>) {
        topicDao.insertFavoriteTopicList(topics.asEntity())
    }

}