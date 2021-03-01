package com.marcossalto.targetmvd.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.marcossalto.targetmvd.network.managers.IUserManager
import com.marcossalto.targetmvd.network.managers.SessionManager
import com.marcossalto.targetmvd.network.managers.UserManager
import com.marcossalto.targetmvd.network.models.AccessTokenSerializer
import com.marcossalto.targetmvd.network.models.UserSignIn
import com.marcossalto.targetmvd.ui.base.BaseViewModel
import com.marcossalto.targetmvd.util.NetworkState
import com.marcossalto.targetmvd.util.ViewModelListener
import com.marcossalto.targetmvd.util.extensions.ApiErrorType
import com.marcossalto.targetmvd.util.extensions.ApiException
import kotlinx.coroutines.launch

open class SignInActivityViewModel(listener: ViewModelListener?) : BaseViewModel(listener) {

    private val manager: IUserManager = UserManager

    var state: SignInState = SignInState.NONE
        set(value) {
            field = value
            listener?.updateState()
        }

    fun signIn(user: UserSignIn) {
        networkState = NetworkState.LOADING
        viewModelScope.launch {
            val result = manager.signIn(user = user)
            if (result.isSuccess) {
                result.getOrNull()?.value?.user?.let { user ->
                    SessionManager.signIn(user)
                }

                networkState = NetworkState.IDLE
                state = SignInState.SUCCESS
            } else {
                handleError(result.exceptionOrNull())
            }
        }
    }

    fun login(accessToken: AccessTokenSerializer) {
        networkState = NetworkState.LOADING
        viewModelScope.launch {
            val result = manager.login(accessToken = accessToken)
            if (result.isSuccess) {
                result.getOrNull()?.value?.user?.let { user ->
                    SessionManager.signIn(user)
                }

                networkState = NetworkState.IDLE
                state = SignInState.SUCCESS
            } else {
                handleError(result.exceptionOrNull())
            }
        }
    }

    private fun handleError(exception: Throwable?) {
        error = if (exception is ApiException && exception.errorType == ApiErrorType.API_ERROR) {
            exception.message
        } else null

        networkState = NetworkState.IDLE
        networkState = NetworkState.ERROR
        state = SignInState.FAILURE
    }
}

enum class SignInState {
    FAILURE,
    SUCCESS,
    NONE,
}

class SignInActivityViewModelFactory(var listener: ViewModelListener?) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SignInActivityViewModel(listener) as T
    }
}
