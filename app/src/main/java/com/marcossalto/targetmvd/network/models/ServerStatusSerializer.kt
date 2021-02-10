package com.marcossalto.targetmvd.network.models

import com.squareup.moshi.Json

data class ServerStatusSerializer(@Json(name = "online") val online: Boolean)
