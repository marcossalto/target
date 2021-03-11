package com.marcossalto.targetmvd.network.models

import com.marcossalto.targetmvd.models.ConversationModel
import com.marcossalto.targetmvd.models.TargetModel
import com.marcossalto.targetmvd.models.TopicModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Target(
    @Json(name = "id") val id: Long = 0,
    @Json(name = "title") val title: String = "",
    @Json(name = "lat") val lat: Double = 0.0,
    @Json(name = "lng") val lng: Double = 0.0,
    @Json(name = "radius") val radius: Double = 0.0,
    @Json(name = "topic_id") val topicId: Int = 0
)

@JsonClass(generateAdapter = true)
data class TargetSerializer(
    @Json(name = "target") val target: Target,
    @Json(name = "match_conversation") val matchConversation: Conversation? = null,
    @Json(name = "matched_user") val matchedUser: MatchedUser? = null
)

@JsonClass(generateAdapter = true)
data class Conversation(
    @Json(name = "id") val id: Long = 0,
    @Json(name = "topic_id") val topicId: Int = 0
)

@JsonClass(generateAdapter = true)
data class TargetsSerializer(@Json(name = "targets") val targets: List<TargetSerializer>)

fun Target.toTargetModel(topic: TopicModel): TargetModel {
    return TargetModel(
        title = title,
        lat = lat,
        lng = lng,
        radius = radius,
        topic = topic,
        id = id
    )
}

fun Conversation.toConversationModel(): ConversationModel {
    return ConversationModel(
        id=id,
        topicId=topicId
    )
}
