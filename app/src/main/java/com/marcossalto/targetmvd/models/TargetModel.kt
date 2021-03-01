package com.marcossalto.targetmvd.models

data class TargetModel(
    val id: Long = 0,
    val title: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val radius: Double = 0.0,
    val topic: TopicModel? = null
)
