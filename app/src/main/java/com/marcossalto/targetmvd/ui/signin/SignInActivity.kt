package com.marcossalto.targetmvd.ui.signin

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.marcossalto.targetmvd.R
import com.marcossalto.targetmvd.databinding.ActivitySignInBinding
import com.marcossalto.targetmvd.metrics.Analytics
import com.marcossalto.targetmvd.metrics.PageEvents
import com.marcossalto.targetmvd.metrics.VISIT_SIGN_IN
import com.marcossalto.targetmvd.network.models.UserSignIn
import com.marcossalto.targetmvd.ui.profile.ProfileActivity
import com.marcossalto.targetmvd.ui.signup.SignUpActivity
import com.marcossalto.targetmvd.ui.view.AuthView
import com.marcossalto.targetmvd.util.NetworkState
import com.marcossalto.targetmvd.util.ViewModelListener
import com.marcossalto.targetmvd.util.extensions.isNotEmpty
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

        with(binding){
            emailEditText.setOnFocusChangeListener { _, b -> clearErrorState(b) }
            passwordEditText.setOnFocusChangeListener { _, b -> clearErrorState(b) }
            signInButton.setOnClickListener { signIn() }
            signUpButton.setOnClickListener { signUp() }
        }

        lifecycle.addObserver(viewModel)

        sampleAskForPermission()
    }


    override fun showProfile() {
        startActivityClearTask(ProfileActivity())
    }

    private fun signIn() {
        if(binding.emailEditText.isNotEmpty() &&
                binding.passwordEditText.isNotEmpty()){
            val userSignIn = UserSignIn(
                email = binding.emailEditText.value(),
                password = binding.passwordEditText.value()
            )
            viewModel.signIn(userSignIn)
        } else {
            showError(getString(R.string.email_or_password_cannot_be_empty))
        }
    }

    private fun signUp() {
        startActivityClearTask(SignUpActivity())
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

    private fun sampleAskForPermission() {
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

    override fun showError(message: String?) {
        if (message != null) {
            with(binding){
                with(errorMessageSignIn){
                    text = message
                    visibility = View.VISIBLE
                }
                emailEditText.background = resources.getDrawable(R.drawable.edit_text_red)
                passwordEditText.background = resources.getDrawable(R.drawable.edit_text_red)
            }

        }
    }

    private fun clearErrorState(b: Boolean) {
        with(binding){
            with(errorMessageSignIn){
                text = ""
                visibility = View.GONE
            }
            emailEditText.background = resources.getDrawable(R.drawable.edit_text_normal)
            passwordEditText.background = resources.getDrawable(R.drawable.edit_text_normal)
        }
    }
}