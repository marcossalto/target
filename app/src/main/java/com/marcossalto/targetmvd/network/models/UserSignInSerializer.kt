package com.marcossalto.targetmvd.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserSignIn(
    @Json(name = "email") val email: String = "",
    @Json(name = "password") val password: String = ""
)

@JsonClass(generateAdapter = true)
data class UserSignInSerializer(@Json(name = "user") val user: UserSignIn)
