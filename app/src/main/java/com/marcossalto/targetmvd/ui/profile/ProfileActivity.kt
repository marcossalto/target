package com.marcossalto.targetmvd.ui.profile

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.marcossalto.targetmvd.R
import com.marcossalto.targetmvd.databinding.ActivityProfileBinding
import com.marcossalto.targetmvd.metrics.Analytics
import com.marcossalto.targetmvd.metrics.PageEvents
import com.marcossalto.targetmvd.metrics.VISIT_PROFILE
import com.marcossalto.targetmvd.network.managers.SessionManager
import com.marcossalto.targetmvd.ui.base.BaseActivity
import com.marcossalto.targetmvd.ui.signin.SignInActivity
import com.marcossalto.targetmvd.ui.view.ProfileView
import com.marcossalto.targetmvd.util.NetworkState
import com.marcossalto.targetmvd.util.ViewModelListener

class ProfileActivity : BaseActivity(), ProfileView {

    private lateinit var viewModel: ProfileActivityViewModel
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ProfileActivityViewModelFactory(viewModelListener)
        viewModel = ViewModelProviders.of(this, factory)
            .get(ProfileActivityViewModel::class.java)

        Analytics.track(PageEvents.visit(VISIT_PROFILE))

        binding.userNameEditText.setText(SessionManager.user?.username)
        binding.emailEditText.setText(SessionManager.user?.email)
        binding.logoutButton.setOnClickListener { viewModel.signOut() }

        lifecycle.addObserver(viewModel)
    }

    override fun goToFirstScreen() {
        startActivityClearTask(SignInActivity())
    }

    // ViewModelListener
    private val viewModelListener = object : ViewModelListener {
        override fun updateState() {
            when (viewModel.state) {
                ProfileState.FAILURE -> showError(viewModel.error)
                ProfileState.SUCCESS -> goToFirstScreen()
                else -> {
                }
            }
        }

        override fun updateNetworkState() {
            when (viewModel.networkState) {
                NetworkState.LOADING -> showProgress()
                NetworkState.IDLE -> hideProgress()
                else -> showError(viewModel.error ?: getString(R.string.default_error))
            }
        }
    }
}
