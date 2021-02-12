package com.marcossalto.targetmvd.network.models

import com.squareup.moshi.Json

data class User (
    @Json(name = "id") val id: Int,
    @Json(name = "email") val email: String,
    @Json(name = "uid") val uid: String,
    @Json(name = "provider") val provider: String,
    @Json(name = "avatar") val avatar: Avatar,
    @Json(name = "first_name") var firstName: String,
    @Json(name = "last_name") var lastName: String,
    @Json(name = "username") val username: String,
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "updated_at") val updatedAt: String,
    @Json(name = "gender") var gender: String,
    @Json(name = "push_token") val pushToken: String,
    @Json( name ="allow_password_change") val allowPasswordChange: Boolean
)

data class Avatar(
    @Json(name = "url") val url: String?,
    @Json(name = "normal") val normal: Normal,
    @Json(name = "small_thumb") val smallThumb: SmallThumb,

)

data class Normal(
    @Json(name = "url") val url: String
)

data class SmallThumb(
    @Json(name = "url") val url: String
)

data class UserSerializer(@Json(name = "user") val user: User)