package com.marcossalto.targetmvd.network.managers

import com.marcossalto.targetmvd.network.models.ConversationsSerializer
import com.marcossalto.targetmvd.network.providers.ServiceProvider
import com.marcossalto.targetmvd.network.services.ApiService
import com.marcossalto.targetmvd.util.extensions.ActionCallback
import com.marcossalto.targetmvd.util.extensions.Data

object ConversationManager: IConversationManager {
    private var service = ServiceProvider.create(ApiService::class.java)
    override suspend fun getConversations(): Result<Data<ConversationsSerializer>> =
        ActionCallback.call(service.getConversations())
}
