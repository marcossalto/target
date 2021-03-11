package com.marcossalto.targetmvd.models

import com.marcossalto.targetmvd.network.models.AvatarMatch

data class MatchedUserModel(
    val id: Int,
    val email: String,
    var firstName: String? = "",
    var lastName: String? = "",
    var fullName: String? = "",
    val username: String,
    var gender: String,
    val avatar: AvatarMatch
)
