package com.marcossalto.targetmvd.network.models

import com.marcossalto.targetmvd.models.MatchedUserModel
import com.marcossalto.targetmvd.models.TargetModel
import com.marcossalto.targetmvd.models.TopicModel
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
    @Json(name = "first_name") var firstName: String? = "",
    @Json(name = "last_name") var lastName: String? = "",
    @Json(name = "username") val username: String,
    @Json(name = "gender") var gender: String,
    @Json(name = "push_token") val pushToken: String?,
    @Json( name ="allow_password_change") val allowPasswordChange: Boolean? = false
)

@JsonClass(generateAdapter = true)
data class Avatar(
    @Json(name = "url") val url: String? = null,
    @Json(name = "normal") val normal: Normal = Normal(null),
    @Json(name = "small_thumb") val smallThumb: SmallThumb = SmallThumb(null)
)

@JsonClass(generateAdapter = true)
data class AvatarMatch(
    @Json(name = "original_url") val url: String? = null,
    @Json(name = "normal_url") val normal: Normal? = Normal(null),
    @Json(name = "small_thumb_url") val smallThumb: SmallThumb? = SmallThumb(null)
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

@JsonClass(generateAdapter = true)
data class UserSignInResponseSerializer(@Json(name = "data") val user: User)

@JsonClass(generateAdapter = true)
data class UserSignIn(
    @Json(name = "email") val email: String = "",
    @Json(name = "password") val password: String = ""
)

@JsonClass(generateAdapter = true)
data class UserSignInSerializer(@Json(name = "user") val user: UserSignIn)

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

@JsonClass(generateAdapter = true)
data class MatchedUser (
    @Json(name = "id") val id: Int,
    @Json(name = "email") val email: String,
    @Json(name = "first_name") var firstName: String? = "",
    @Json(name = "last_name") var lastName: String? = "",
    @Json(name = "full_name") var fullName: String? = "",
    @Json(name = "username") val username: String,
    @Json(name = "gender") var gender: String,
    @Json(name = "avatar") val avatar: AvatarMatch
)

fun MatchedUser.toMatchedUserModel(): MatchedUserModel {
    return MatchedUserModel(
        id=id,
        email=email,
        firstName=firstName,
        lastName=lastName,
        fullName=fullName,
        username=username,
        gender=gender,
        avatar=avatar
    )
}
