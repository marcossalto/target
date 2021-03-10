package com.marcossalto.targetmvd.models

import  com.marcossalto.targetmvd.network.models.Target

data class TargetModel(
    val id: Long = 0,
    val title: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val radius: Double = 0.0,
    val topic: TopicModel
)

fun TargetModel.toTargetRequest(): Target {
    return Target(
        id = id,
        title = title,
        lat = lat,
        lng = lng,
        radius = radius,
        topicId = topic.id
    )
}
