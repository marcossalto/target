package com.marcossalto.targetmvd.ui.chatlist

import androidx.lifecycle.*
import com.marcossalto.targetmvd.models.TargetModel
import com.marcossalto.targetmvd.models.TopicModel
import com.marcossalto.targetmvd.network.managers.ConversationManager
import com.marcossalto.targetmvd.network.managers.IConversationManager
import com.marcossalto.targetmvd.network.managers.LocationManager
import com.marcossalto.targetmvd.network.managers.TargetManager
import com.marcossalto.targetmvd.network.models.ChatConversation
import com.marcossalto.targetmvd.network.models.ConversationsSerializer
import com.marcossalto.targetmvd.network.models.Target
import com.marcossalto.targetmvd.network.models.toTargetModel
import com.marcossalto.targetmvd.ui.base.BaseViewModel
import com.marcossalto.targetmvd.ui.target.ActionOnTargetState
import com.marcossalto.targetmvd.ui.target.TargetActivityViewModel
import com.marcossalto.targetmvd.util.NetworkState
import com.marcossalto.targetmvd.util.ViewModelListener
import kotlinx.coroutines.launch

class ChatListActivityViewModel(private val conversationManager: IConversationManager,
                                listener: ViewModelListener?) : BaseViewModel(listener) {
    var networkStateObservable: MutableLiveData<NetworkState> = MutableLiveData()
    private val conversations: MutableLiveData<List<ChatConversation>> = MutableLiveData()

    fun getConversations(): LiveData<List<ChatConversation>> {
        networkStateObservable.postValue(NetworkState.LOADING)
        viewModelScope.launch {
            val result = conversationManager.getConversations()
            if (result.isSuccess) {
                val values: List<ChatConversation> = result
                    .getOrNull()?.value?.matches?.map { it } ?: emptyList()
                conversations.postValue(values)
                networkStateObservable.postValue(NetworkState.IDLE)
            } else {
                conversations.postValue(emptyList())
                networkStateObservable.postValue(NetworkState.IDLE)
            }
        }
        return conversations
    }

    fun onChatClicked(chat: ChatConversation) = viewModelScope.launch { }
}

class ChatListActivityViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChatListActivityViewModel(ConversationManager, null)  as T
    }
}
