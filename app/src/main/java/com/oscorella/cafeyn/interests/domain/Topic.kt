package com.oscorella.cafeyn.interests.domain


data class Topic (
    val id: String,
    val name: Name,
    val subTopics: List<Topic>,
    var index: Int = -1
)

data class Name (
    val raw: String,
    val key: String = ""
)

class TopicComparator : Comparator<Topic> {
    override fun compare(topic1: Topic, topic2: Topic): Int {
        return topic1.index - topic2.index
    }
}