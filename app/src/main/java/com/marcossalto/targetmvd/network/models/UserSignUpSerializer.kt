package com.marcossalto.targetmvd.network.models

import com.squareup.moshi.Json

data class UserSignUp(
    @Json(name = "username") val userName: String = "",
    var email: String = "",
    var gender: String = "",
    val password: String = "",
    @Json(name = "password_confirmation") val passwordConfirmation: String = ""
)

data class UserSignUpSerializer(@Json(name = "user") val user: UserSignUp)
