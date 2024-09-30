package com.oscorella.cafeyn.core.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.oscorella.cafeyn.interests.domain.Name

@Entity
data class TopicEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val index: Int
)