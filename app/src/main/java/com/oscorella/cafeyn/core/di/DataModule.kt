package com.oscorella.cafeyn.core.di

import com.oscorella.cafeyn.interests.data.TopicRepository
import com.oscorella.cafeyn.interests.data.TopicRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsAuthenticationRepo(
        topicRepositoryImpl: TopicRepositoryImpl
    ): TopicRepository

}