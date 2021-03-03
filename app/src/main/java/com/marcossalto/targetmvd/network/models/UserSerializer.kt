package com.marcossalto.targetmvd.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson

@JsonClass(generateAdapter = true)
data class User (
    @Json(name = "id") val id: Int,
    @Json(name = "email") val email: String,
    @Json(name = "uid") val uid: String,
    @Json(name = "provider") val provider: String,
    @Json(name = "avatar") val avatar: Avatar,
    @Json(name = "first_name") var firstName: String?,
    @Json(name = "last_name") var lastName: String?,
    @Json(name = "username") val username: String,
    @Json(name = "gender") var gender: String,
    @Json(name = "push_token") val pushToken: String?,
    @Json( name ="allow_password_change") val allowPasswordChange: Boolean?
)

@JsonClass(generateAdapter = true)
data class Avatar(
    @Json(name = "url") val url: String? = null,
    @Json(name = "normal") val normal: Normal,
    @Json(name = "small_thumb") val smallThumb: SmallThumb,

    )

@JsonClass(generateAdapter = true)
data class Normal(
    @Json(name = "url") val url: String? = null
)

@JsonClass(generateAdapter = true)
data class SmallThumb(
    @Json(name = "url") val url: String? = null
)

@JsonClass(generateAdapter = true)
data class UserSerializer(@Json(name = "user") val user: User)
