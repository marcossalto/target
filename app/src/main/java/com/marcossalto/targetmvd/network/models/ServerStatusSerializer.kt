package com.marcossalto.targetmvd.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ServerStatusSerializer(@Json(name = "online") val online: Boolean)
