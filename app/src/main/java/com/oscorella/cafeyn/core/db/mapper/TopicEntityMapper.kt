package com.oscorella.cafeyn.core.db.mapper

import com.oscorella.cafeyn.core.db.TopicEntity
import com.oscorella.cafeyn.interests.domain.Name
import com.oscorella.cafeyn.interests.domain.Topic

object TopicEntityMapper : EntityMapper<List<Topic>, List<TopicEntity>> {

    override fun asEntity(domain: List<Topic>): List<TopicEntity> {
        return domain.map { topic ->
            TopicEntity(
                id = topic.id,
                name = topic.name.raw,
                index = topic.index
            )
        }
    }

    override fun asDomain(entity: List<TopicEntity>): List<Topic> {
        return entity.map { topicEntity ->
            Topic(
                id = topicEntity.id,
                name = Name(topicEntity.name),
                subTopics = emptyList(),
                index = topicEntity.index
            )
        }
    }
}

fun List<Topic>.asEntity(): List<TopicEntity> {
    return TopicEntityMapper.asEntity(this)
}

fun List<TopicEntity>?.asDomain(): List<Topic> {
    return TopicEntityMapper.asDomain(this.orEmpty())
}