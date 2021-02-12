package com.marcossalto.targetmvd.network.models

import com.squareup.moshi.Json

data class UserSignIn(
    val email: String = "",
    val password: String = ""

)

data class UserSignInSerializer(@Json(name = "user") val user: UserSignIn)