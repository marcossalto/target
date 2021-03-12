package com.marcossalto.targetmvd.network.managers

import com.marcossalto.targetmvd.network.models.ConversationsSerializer
import com.marcossalto.targetmvd.util.extensions.Data

interface IConversationManager {
    suspend fun getConversations(): Result<Data<ConversationsSerializer>>
}
