package com.marcossalto.targetmvd.network.models

import com.squareup.moshi.Json

data class User (
    @Json(name = "id") val id: Int,
    @Json(name = "email") val email: String,
    @Json(name = "uid") val uid: String,
    @Json(name = "provider") val provider: String,
    @Json(name = "avatar") val avatar: Avatar,
    @Json(name = "first_name") var first_name: String?,
    @Json(name = "last_name") var last_name: String?,
    @Json(name = "username") val username: String,
    @Json(name = "gender") var gender: String,
    @Json(name = "push_token") val push_token: String?,
    @Json( name ="allow_password_change") val allow_password_change: Boolean?
)

data class Avatar(
    @Json(name = "url") val url: String? = null,
    @Json(name = "normal") val normal: Normal,
    @Json(name = "small_thumb") val small_thumb: SmallThumb,

    )

data class Normal(
    @Json(name = "url") val url: String? = null
)

data class SmallThumb(
    @Json(name = "url") val url: String? = null
)

data class UserSerializer(@Json(name = "user") val user: User)