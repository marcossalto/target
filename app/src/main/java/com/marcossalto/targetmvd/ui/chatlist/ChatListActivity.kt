package com.marcossalto.targetmvd.ui.chatlist

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.marcossalto.targetmvd.R
import com.marcossalto.targetmvd.databinding.ActivityChatListBinding
import com.marcossalto.targetmvd.metrics.Analytics
import com.marcossalto.targetmvd.metrics.PageEvents
import com.marcossalto.targetmvd.metrics.VISIT_CHAT_LIST
import com.marcossalto.targetmvd.network.models.ChatConversation
import com.marcossalto.targetmvd.ui.base.BaseActivity
import com.marcossalto.targetmvd.ui.profile.ProfileActivity
import com.marcossalto.targetmvd.ui.target.TargetActivity
import com.marcossalto.targetmvd.util.NetworkState
import kotlinx.android.synthetic.main.toolbar.*

class ChatListActivity : BaseActivity() {
    private lateinit var viewModel: ChatListActivityViewModel
    private lateinit var binding: ActivityChatListBinding
    private lateinit var adapter: ChatAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Analytics.track(PageEvents.visit(VISIT_CHAT_LIST))

        val factory = ChatListActivityViewModelFactory()
        viewModel = ViewModelProvider(this, factory)
            .get(ChatListActivityViewModel::class.java)
        observeNetworkState()
        initView()
    }

    private fun observeNetworkState() {
        viewModel.networkStateObservable.observe(this, Observer { state ->
            state?.run {
                when (state) {
                    NetworkState.LOADING -> showProgress()
                    NetworkState.ERROR, NetworkState.IDLE -> hideProgress()
                }
            }
        })
    }

    private fun initView() {
        with(binding.toolbar) {
            profileImageView.setOnClickListener {
                startActivityClearTask(ProfileActivity())
            }
            toolbarTitleTextView.text = getString(R.string.chat)
            chatImageView.visibility = View.GONE
            markerImageView.visibility = View.VISIBLE
            markerImageView.setOnClickListener {
                startActivityClearTask(TargetActivity())
            }
        }
        adapter = ChatAdapter(viewModel::onChatClicked)
        binding.recycler.adapter = adapter
        observeConversationsState()
    }

    private fun observeConversationsState() {
        viewModel.getConversations().observe(this, { updateUi(it) })
    }

    private fun updateUi(list: List<ChatConversation>) {
        adapter.chats = list
        adapter.notifyDataSetChanged()
    }
}
