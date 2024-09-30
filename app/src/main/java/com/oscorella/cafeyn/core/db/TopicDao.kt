package com.oscorella.cafeyn.core.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TopicDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteTopicList(topics: List<TopicEntity>)

    @Delete
    public fun deleteFavoriteTopics(topics: List<TopicEntity>)

    @Query("SELECT * FROM TopicEntity")
    suspend fun getAllFavoriteTopics(): List<TopicEntity>


}
