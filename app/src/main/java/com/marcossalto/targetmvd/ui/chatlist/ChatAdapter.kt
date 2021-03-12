package com.marcossalto.targetmvd.ui.chatlist

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marcossalto.targetmvd.R
import com.marcossalto.targetmvd.databinding.ViewChatBinding
import com.marcossalto.targetmvd.network.models.ChatConversation
import com.marcossalto.targetmvd.util.extensions.basicDiffUtil
import com.marcossalto.targetmvd.util.extensions.inflate
import com.marcossalto.targetmvd.util.extensions.load

class ChatAdapter(private val listener: (ChatConversation) -> Unit)  :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    var chats: List<ChatConversation> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.id == new.id }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.view_chat, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = chats.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chats[position]
        with(holder){
            bind(chat)
            itemView.setOnClickListener {
                listener(chat)
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ViewChatBinding.bind(view)
        fun bind(chat: ChatConversation) {
            with (binding){
                userNameTextView.text = chat.user.fullName
                lastMessageTextView.text = chat.lastMessage
                topicImageView.load(chat.topicIcon)
                chat.user.avatar.normalUrl?.let { avatar.load(it) }
            }
        }
    }
}
