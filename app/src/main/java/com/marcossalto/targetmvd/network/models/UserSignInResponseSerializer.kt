package com.marcossalto.targetmvd.network.models

import com.squareup.moshi.Json

data class UserSignInResponseSerializer(@Json(name = "data") val data: User)
