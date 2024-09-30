package com.oscorella.cafeyn.interests.data

import com.oscorella.cafeyn.interests.domain.Topic
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface TopicService {

    @GET("b2c/topics/signup")
    @Headers(
        "Content-Type: application/json",
        "User-Agent: Android App",
        "Accept: */*"
    )
    suspend fun getTopics(
        @Query("maxDepth") maxDepth: String = "2",
    ): Response<List<Topic>>

}