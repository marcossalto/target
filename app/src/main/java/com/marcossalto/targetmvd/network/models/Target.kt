package com.marcossalto.targetmvd.network.models

import com.marcossalto.targetmvd.models.TargetModel
import com.marcossalto.targetmvd.models.TopicModel
import com.squareup.moshi.Json

data class Target(
    @Json(name = "id") val id: Long = 0,
    @Json(name = "title") val title: String = "",
    @Json(name = "lat") val lat: Double = 0.0,
    @Json(name = "lng") val lng: Double = 0.0,
    @Json(name = "radius") val radius: Double = 0.0,
    @Json(name = "topic_id") val topic_id: Int = 0
)

data class TargetSerializer(@Json(name = "target") val target: Target)

data class TargetsSerializer(@Json(name = "targets") val targets: List<TargetSerializer>)

fun Target.toTargetModel(topic: TopicModel?): TargetModel {
    return TargetModel(
        title = title,
        lat = lat,
        lng = lng,
        radius = radius,
        topic = topic,
        id = id
    )
}
