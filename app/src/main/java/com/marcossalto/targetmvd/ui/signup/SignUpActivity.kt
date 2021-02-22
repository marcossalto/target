package com.marcossalto.targetmvd.ui.signup

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.ViewModelProviders
import com.marcossalto.targetmvd.R
import com.marcossalto.targetmvd.databinding.ActivitySignUpBinding
import com.marcossalto.targetmvd.metrics.Analytics
import com.marcossalto.targetmvd.metrics.PageEvents
import com.marcossalto.targetmvd.metrics.VISIT_SIGN_UP
import com.marcossalto.targetmvd.network.models.UserSignUp
import com.marcossalto.targetmvd.ui.base.BaseActivity
import com.marcossalto.targetmvd.ui.signin.SignInActivity
import com.marcossalto.targetmvd.ui.view.AuthView
import com.marcossalto.targetmvd.util.NetworkState
import com.marcossalto.targetmvd.util.ViewModelListener
import com.marcossalto.targetmvd.util.extensions.*

class SignUpActivity : BaseActivity(), AuthView {

    private lateinit var viewModel: SignUpActivityViewModel
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Analytics.track(PageEvents.visit(VISIT_SIGN_UP))

        val factory = SignUpActivityViewModelFactory(viewModelListener)
        viewModel = ViewModelProviders.of(this, factory)
            .get(SignUpActivityViewModel::class.java)

        with(binding) {
            nameEditText.setOnFocusChangeListener { _, b -> clearErrorState(b) }
            emailEditText.setOnFocusChangeListener { _, b -> clearErrorState(b) }
            passwordEditText.setOnFocusChangeListener { _, b -> clearErrorState(b) }
            confirmPasswordEditText.setOnFocusChangeListener { _, b -> clearErrorState(b) }
            genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    clearErrorState(false)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
            signUpButtonSignUp.setOnClickListener { signUp() }
            signInButtonSignUp.setOnClickListener { signIn() }
        }

        lifecycle.addObserver(viewModel)
    }

    override fun showFeed() {
        toast("Navigate to FeedActivity")
    }

    private fun signUp() {
        clearErrorState(false)
        with(binding) {
            when {
                !nameEditText.isNotEmpty() -> {
                    with(nameMessageTextView) {
                        text = getString(R.string.you_forgot_to_put_your_name)
                        visibility = android.view.View.VISIBLE
                    }
                    nameEditText.background = resources.getDrawable(R.drawable.edit_text_red, null)
                }
                !emailEditText.value().isEmail() -> {
                    with(emailMessageTextView) {
                        text = getString(R.string.oops_this_email_is_not_valid)
                        visibility = android.view.View.VISIBLE
                    }
                    emailEditText.background = resources.getDrawable(R.drawable.edit_text_red, null)
                }
                !passwordEditText.validate("[a-zA-Z0-9]{6,}") -> {
                    with(passwordMessageTextView) {
                        text = getString(R.string.the_password_must_be_6_character_long)
                        visibility = android.view.View.VISIBLE
                    }
                    passwordEditText.background = resources.getDrawable(R.drawable.edit_text_red, null)
                }
                confirmPasswordEditText.value() != passwordEditText.value() -> {
                    with(confirmPasswordMessage) {
                        text =
                            getString(R.string.password_don_t_match)
                        visibility = android.view.View.VISIBLE
                    }
                    confirmPasswordEditText.background =
                        resources.getDrawable(R.drawable.edit_text_red, null)
                }
                genderSpinner.selectedItemId.toInt() == 0 -> {
                    with(genderMessageTextView) {
                        text =
                            getString(R.string.you_forgot_to_select_your_gender)
                        visibility = android.view.View.VISIBLE
                    }
                }
                else -> {
                    val userSignUp = UserSignUp(
                        userName = nameEditText.value().replace(" ","").trim().toLowerCase(),
                        email = emailEditText.value(),
                        gender = genderSpinner.getItemAtPosition(genderSpinner.selectedItemPosition).toString().toLowerCase(),
                        password = passwordEditText.value(),
                        passwordConfirmation = confirmPasswordEditText.value()
                    )
                    viewModel.signUp(userSignUp)
                }
            }
        }
    }

    private fun signIn() {
        startActivityClearTask(SignInActivity())
    }

    // ViewModelListener
    private val viewModelListener = object : ViewModelListener {
        override fun updateState() {
            when (viewModel.state) {
                SignUpState.FAILURE -> showError(viewModel.error)
                SignUpState.SUCCESS -> showFeed()
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

    override fun showError(message: String?) {
        if (message != null) {
            toast(message)
        }
    }

    private fun clearErrorState(b: Boolean) {
        with(binding) {
            with(nameMessageTextView) {
                text = ""
                visibility = android.view.View.GONE
            }
            with(emailMessageTextView) {
                text = ""
                visibility = android.view.View.GONE
            }
            with(passwordMessageTextView) {
                text = ""
                visibility = android.view.View.GONE
            }
            with(confirmPasswordMessage) {
                text = ""
                visibility = android.view.View.GONE
            }
            with(genderMessageTextView) {
                text = ""
                visibility = android.view.View.GONE
            }
            resources.getDrawable(R.drawable.edit_text_normal, null)
                .also {
                    nameEditText.background = it
                    emailEditText.background = it
                    passwordEditText.background = it
                    confirmPasswordEditText.background = it
                }
        }
    }
}
