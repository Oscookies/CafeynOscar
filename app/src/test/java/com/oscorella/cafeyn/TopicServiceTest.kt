package com.oscorella.cafeyn

import com.oscorella.cafeyn.interests.data.TopicService
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.net.SocketTimeoutException
import okhttp3.mockwebserver.MockWebServer

class TopicServiceTest {

    @get:Rule
    val mockWebServer = MockWebServer()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val topicService by lazy {
        retrofit.create(TopicService::class.java)
    }

    @Test
    fun getTopicsSuccess() = runTest {
        mockWebServer.enqueue(MockResponse().setBody(getTopicsResponse).setResponseCode(200))
        val result = topicService.getTopics()
        assert(result.body()?.size == 2)
    }

    @Test
    fun getTopicsTimeout() {
        mockWebServer.enqueue(MockResponse().setBody("").setResponseCode(408))
        runBlocking {
            try {
                topicService.getTopics()
            }
            catch (e: Exception){
                assert(e is SocketTimeoutException)
            }

        }
    }
}

const val getTopicsResponse =
    """
[
    {
        "id": "Q38926",
        "name": {
            "raw": "News & Politics",
            "key": "topic.Q38926"
        },
        "color": "#59AAC7",
        "subTopics": [
            {
                "id": "Q5468197",
                "name": {
                    "raw": "International news ",
                    "key": "topic.Q5468197"
                },
                "color": "#8690E4",
                "parentIds": [
                    "Q38926"
                ]
            },
            {
                "id": "Q83267",
                "name": {
                    "raw": "Crime & justice ",
                    "key": "topic.Q83267"
                },
                "color": "#CD8E8A",
                "parentIds": [
                    "Q38926"
                ]
            }
        ]
    },
    {
        "id": "Q2138556",
        "name": {
            "raw": "Regions",
            "key": "topic.Q2138556"
        },
        "color": "#266BBB",
        "subTopics": [
            {
                "id": "Q13917",
                "name": {
                    "raw": "Île-De-France",
                    "key": "topic.Q13917"
                },
                "color": "#8690E4",
                "parentIds": [
                    "Q2138556"
                ]
            },
            {
                "id": "Q18678082",
                "name": {
                    "raw": "Nouvelle Aquitaine",
                    "key": "topic.Q18678082"
                },
                "color": "#EF9A9A",
                "parentIds": [
                    "Q2138556"
                ]
            },
            {
                "id": "Q18677767",
                "name": {
                    "raw": "Hauts-de-France",
                    "key": "topic.Q18677767"
                },
                "color": "#CD8E8A",
                "parentIds": [
                    "Q2138556"
                ]
            },
            {
                "id": "Q15104",
                "name": {
                    "raw": "PACA",
                    "key": "topic.Q15104"
                },
                "color": "#E4A366",
                "parentIds": [
                    "Q2138556"
                ]
            },
            {
                "id": "Q18338206",
                "name": {
                    "raw": "Auvergne Rhône Alpes",
                    "key": "topic.Q18338206"
                },
                "color": "#266BBB",
                "parentIds": [
                    "Q2138556"
                ]
            },
            {
                "id": "Q18578267",
                "name": {
                    "raw": "Bourgogne Franche Comté",
                    "key": "topic.Q18578267"
                },
                "color": "#8690E4",
                "parentIds": [
                    "Q2138556"
                ]
            },
            {
                "id": "Q12130",
                "name": {
                    "raw": "Bretagne",
                    "key": "topic.Q12130"
                },
                "color": "#266BBB",
                "parentIds": [
                    "Q2138556"
                ]
            },
            {
                "id": "Q13947",
                "name": {
                    "raw": "Centre Val de Loire",
                    "key": "topic.Q13947"
                },
                "color": "#58AFAA",
                "parentIds": [
                    "Q2138556"
                ]
            },
            {
                "id": "Q14112",
                "name": {
                    "raw": "Corse",
                    "key": "topic.Q14112"
                },
                "color": "#EF9A9A",
                "parentIds": [
                    "Q2138556"
                ]
            },
            {
                "id": "Q18677983",
                "name": {
                    "raw": "Grand Est",
                    "key": "topic.Q18677983"
                },
                "color": "#6A596A",
                "parentIds": [
                    "Q2138556"
                ]
            },
            {
                "id": "Q203396",
                "name": {
                    "raw": "Outre-mer",
                    "key": "topic.Q203396"
                },
                "color": "#CBC75D",
                "parentIds": [
                    "Q2138556"
                ]
            },
            {
                "id": "Q18677875",
                "name": {
                    "raw": "Normandie",
                    "key": "topic.Q18677875"
                },
                "color": "#266BBB",
                "parentIds": [
                    "Q2138556"
                ]
            },
            {
                "id": "Q18678265",
                "name": {
                    "raw": "Occitanie",
                    "key": "topic.Q18678265"
                },
                "color": "#849A7B",
                "parentIds": [
                    "Q2138556"
                ]
            },
            {
                "id": "Q16994",
                "name": {
                    "raw": "Pays de la Loire",
                    "key": "topic.Q16994"
                },
                "color": "#266BBB",
                "parentIds": [
                    "Q2138556"
                ]
            }
        ]
    }
]
"""