package com.marcossalto.targetmvd.network.models

import com.squareup.moshi.Json

data class UserSignUp(
    @Json(name = "username") val username: String = "",
    var email: String = "",
    var gender: String = "",
    val password: String = "",
    @Json(name = "password_confirmation") val password_confirmation: String = ""
)

data class UserSignUpSerializer(@Json(name = "user") val user: UserSignUp)
