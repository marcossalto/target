package com.marcossalto.targetmvd.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorModel(
    @Json(name = "errors") val errors: List<String>
)

@JsonClass(generateAdapter = true)
data class UnprocessableEntity(
    @Json(name = "status") val status: String,
    @Json(name = "data") val data: User,
    @Json(name = "errors") val errors: Errors
)

@JsonClass(generateAdapter = true)
data class Errors(
    @Json(name = "email") val email: List<String>,
    @Json(name = "uid") val uid: List<String>,
    @Json(name = "full_messages") val full_messages: List<String>
)

@JsonClass(generateAdapter = true)
data class ErrorBadRequest(
    @Json(name = "errors") val errors: UserError
)

@JsonClass(generateAdapter = true)
data class UserError(
    @Json(name = "user") val user: List<String>
)
