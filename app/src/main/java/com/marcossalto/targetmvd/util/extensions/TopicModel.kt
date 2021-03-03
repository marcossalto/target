package com.marcossalto.targetmvd.util.extensions

import com.marcossalto.targetmvd.R
import com.marcossalto.targetmvd.models.TopicModel
import com.marcossalto.targetmvd.models.TopicTypes

fun TopicModel.getTargetIcon(): Int {
    return when (label) {
        TopicTypes.FOOTBALL -> R.drawable.ic_football
        TopicTypes.TRAVEL -> R.drawable.ic_travel
        TopicTypes.POLITICS -> R.drawable.ic_politics
        TopicTypes.ART -> R.drawable.ic_art
        TopicTypes.DATING -> R.drawable.ic_dating
        TopicTypes.MUSIC -> R.drawable.ic_music
        TopicTypes.MOVIES -> R.drawable.ic_movies
        TopicTypes.SERIES -> R.drawable.ic_series
        TopicTypes.FOOD -> R.drawable.ic_food
        TopicTypes.CHESS -> R.drawable.ic_chess
        TopicTypes.MATE -> R.drawable.ic_mate
    }
}
