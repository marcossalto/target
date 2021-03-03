package com.marcossalto.targetmvd.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserSignUp(
    @Json(name = "username") val username: String = "",
    @Json(name = "email") var email: String = "",
    @Json(name = "gender") var gender: String = "",
    @Json(name = "password") val password: String = "",
    @Json(name = "password_confirmation") val passwordConfirmation: String = ""
)

@JsonClass(generateAdapter = true)
data class UserSignUpSerializer(@Json(name = "user") val user: UserSignUp)
