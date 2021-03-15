package com.marcossalto.targetmvd.ui.target

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.marcossalto.targetmvd.R
import com.marcossalto.targetmvd.databinding.ActivityTargetBinding
import com.marcossalto.targetmvd.metrics.Analytics
import com.marcossalto.targetmvd.metrics.PageEvents
import com.marcossalto.targetmvd.metrics.VISIT_TARGET
import com.marcossalto.targetmvd.ui.chatlist.ChatListActivity
import com.marcossalto.targetmvd.ui.profile.ProfileActivity
import com.marcossalto.targetmvd.util.NetworkState
import com.marcossalto.targetmvd.util.permissions.PermissionActivity

class TargetActivity : PermissionActivity() {
    private lateinit var viewModel: TargetActivityViewModel
    private lateinit var binding: ActivityTargetBinding
    private lateinit var targetView: TargetView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTargetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Analytics.track(PageEvents.visit(VISIT_TARGET))

        val factory = TargetActivityViewModelFactory()
        viewModel = ViewModelProvider(this, factory)
            .get(TargetActivityViewModel::class.java)
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
        initMap()
        with(binding.toolbar) {
            profileImageView.setOnClickListener {
                startActivityClearTask(ProfileActivity())
            }
            chatImageView.setOnClickListener {
                startActivityClearTask(ChatListActivity())
            }
        }
        initTargetView()
    }

    private fun initMap() {
        val fragment = MapFragment.getInstance()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_map, fragment).commit()
    }

    private fun initTargetView() {
        targetView = TargetView(viewModel, this, binding)
        binding.targetLinearLayout.setOnClickListener {
            targetView.expandCollapseCreateTargetSheet()
        }
    }

    companion object {
        const val PICK_HEIGHT_HIDDEN = 0
    }
}
