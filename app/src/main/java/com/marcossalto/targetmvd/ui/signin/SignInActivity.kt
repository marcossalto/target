package com.marcossalto.targetmvd.ui.signin

import android.Manifest
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.marcossalto.targetmvd.R
import com.marcossalto.targetmvd.databinding.ActivitySignInBinding
import com.marcossalto.targetmvd.metrics.Analytics
import com.marcossalto.targetmvd.metrics.PageEvents
import com.marcossalto.targetmvd.metrics.VISIT_SIGN_IN
import com.marcossalto.targetmvd.network.models.UserSignIn
import com.marcossalto.targetmvd.ui.view.AuthView
import com.marcossalto.targetmvd.util.NetworkState
import com.marcossalto.targetmvd.util.ViewModelListener
import com.marcossalto.targetmvd.util.extensions.toast
import com.marcossalto.targetmvd.util.extensions.value
import com.marcossalto.targetmvd.util.permissions.PermissionActivity
import com.marcossalto.targetmvd.util.permissions.PermissionResponse

class SignInActivity : PermissionActivity(), AuthView {
    private lateinit var viewModel: SignInActivityViewModel
    private lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Analytics.track(PageEvents.visit(VISIT_SIGN_IN))

        val factory = SignInActivityViewModelFactory(viewModelListener)
        viewModel = ViewModelProviders.of(this, factory)
            .get(SignInActivityViewModel::class.java)

        binding.signInButton.setOnClickListener { signIn() }

        lifecycle.addObserver(viewModel)

        sampleAskForPermission()
    }


    override fun showProfile() {
        toast("Show Profile Activity")
    }

    private fun signIn() {
        val userSignIn = UserSignIn(
            email = binding.emailEditText.value(),
            password = binding.passwordEditText.value()
        )
        viewModel.signIn(userSignIn)
    }

    // ViewModelListener
    private val viewModelListener = object : ViewModelListener {
        override fun updateState() {
            when (viewModel.state) {
                SignInState.FAILURE -> showError(viewModel.error)
                SignInState.SUCCESS -> showProfile()
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

    fun sampleAskForPermission() {
        requestPermission(arrayOf(Manifest.permission.CAMERA), object : PermissionResponse {
            override fun granted() {
                // TODO..
            }

            override fun denied() {
                // TODO..
            }

            override fun foreverDenied() {
                // TODO..
            }
        })
    }
}