package com.oscorella.cafeyn.core.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TopicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteTopicList(topics: List<TopicEntity>)

    @Query("DELETE FROM TopicEntity")
    suspend fun deleteFavoriteTopics()

    @Query("SELECT * FROM TopicEntity")
    suspend fun getAllFavoriteTopics(): List<TopicEntity>


}
