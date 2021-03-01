package com.marcossalto.targetmvd.models

data class TopicModel(
    val id: Int,
    val label: TopicTypes,
    val icon: String = ""
)

enum class TopicTypes {
    CHESS,
    FOOTBALL,
    TRAVEL,
    POLITICS,
    ART,
    DATING,
    MUSIC,
    MOVIES,
    SERIES,
    FOOD,
    MATE
}
