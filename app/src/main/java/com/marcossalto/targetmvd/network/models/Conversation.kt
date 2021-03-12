package com.marcossalto.targetmvd.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ConversationsSerializer (
    @Json(name = "matches") val matches: List<ChatConversation>
)

@JsonClass(generateAdapter = true)
data class ChatConversation(
    @Json(name = "match_id") val id: Long = 0,
    @Json(name = "topic_icon") val topicIcon: String = "",
    @Json(name = "last_message") val lastMessage: String?,
    @Json(name = "unread_messages") val unreadMessages: Long = 0,
    @Json(name = "user") val user: ChatUser
)

@JsonClass(generateAdapter = true)
data class ChatUser(
    @Json(name = "id") val id: Long = 0,
    @Json(name = "full_name") val fullName: String = "",
    @Json(name = "avatar") val avatar: ChatAvatar
)

@JsonClass(generateAdapter = true)
data class ChatAvatar(
    @Json(name = "original_url") val originalUrl: String? = null,
    @Json(name = "normal_url") val normalUrl: String? = null,
    @Json(name = "small_thumb_url") val smallThumbUrl: String? = null
)

