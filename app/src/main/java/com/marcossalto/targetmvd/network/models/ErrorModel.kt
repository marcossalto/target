package com.marcossalto.targetmvd.network.models

import com.squareup.moshi.Json

data class ErrorModel(
    @Json(name = "errors") val errors: List<String>
)

data class ErrorModelSerializer(val error: ErrorModel)

data class UnprocessableEntity(
    @Json(name = "status") val status: String,
    @Json(name = "data") val data: User,
    @Json(name = "errors") val errors: Errors
)

data class Errors(
    @Json(name = "email") val email: List<String>,
    @Json(name = "uid") val uid: List<String>,
    @Json(name = "full_messages") val full_messages: List<String>
)
