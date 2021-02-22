package com.marcossalto.targetmvd.ui.signin

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.marcossalto.targetmvd.R
import com.marcossalto.targetmvd.databinding.ActivitySignInBinding
import com.marcossalto.targetmvd.metrics.Analytics
import com.marcossalto.targetmvd.metrics.PageEvents
import com.marcossalto.targetmvd.metrics.VISIT_SIGN_IN
import com.marcossalto.targetmvd.network.models.AccessTokenSerializer
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
import java.util.*


class SignInActivity : PermissionActivity(), AuthView {
    private lateinit var viewModel: SignInActivityViewModel
    private lateinit var binding: ActivitySignInBinding
    private var callbackManager: CallbackManager? = null

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
            loginButton.setOnClickListener { login() }
        }

        lifecycle.addObserver(viewModel)

        sampleAskForPermission()

        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired

        if(isLoggedIn){
            toast("Sesion iniciada con FB $accessToken" )
        }

    }

    override fun showFeed() {
        toast("Navigate to FeedActivity")
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

    private fun login(){
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logInWithReadPermissions(
            this, Arrays.asList(
                "public_profile",
                "email"
            )
        )
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    val accessToken = AccessTokenSerializer(
                        access_token = loginResult.accessToken.token
                    )
                    viewModel.login(accessToken)
                }

                override fun onCancel() {
                    Log.d("MainActivity", "Facebook onCancel.")

                }

                override fun onError(error: FacebookException) {
                    Log.d("MainActivity", "Facebook onError.")

                }
            })
    }

    // ViewModelListener
    private val viewModelListener = object : ViewModelListener {
        override fun updateState() {
            when (viewModel.state) {
                SignInState.FAILURE -> showError(viewModel.error)
                SignInState.SUCCESS -> showFeed()
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
                resources.getDrawable(R.drawable.edit_text_red, null)
                    .also {
                        emailEditText.background = it
                        passwordEditText.background = it
                    }
            }

        }
    }

    private fun clearErrorState(b: Boolean) {
        with(binding){
            with(errorMessageSignIn){
                text = ""
                visibility = View.GONE
            }
            resources.getDrawable(R.drawable.edit_text_normal, null)
                .also {
                    emailEditText.background = it
                    passwordEditText.background = it
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }
}