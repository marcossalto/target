package com.marcossalto.targetmvd.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.marcossalto.targetmvd.network.managers.IUserManager
import com.marcossalto.targetmvd.network.managers.SessionManager
import com.marcossalto.targetmvd.network.managers.UserManager
import com.marcossalto.targetmvd.network.models.UserSignUp
import com.marcossalto.targetmvd.ui.base.BaseViewModel
import com.marcossalto.targetmvd.util.NetworkState
import com.marcossalto.targetmvd.util.ViewModelListener
import com.marcossalto.targetmvd.util.extensions.ApiErrorType
import com.marcossalto.targetmvd.util.extensions.ApiException
import kotlinx.coroutines.launch

open class SignUpActivityViewModel(listener: ViewModelListener?) : BaseViewModel(listener) {

    private val manager: IUserManager = UserManager

    var state: SignUpState = SignUpState.NONE
        set(value) {
            field = value
            listener?.updateState()
        }

    fun signUp(user: UserSignUp) {
        networkState = NetworkState.LOADING
        viewModelScope.launch {
            val result = manager.signUp(user = user)

            if (result.isSuccess) {
                result.getOrNull()?.value?.let { user ->
                    SessionManager.signIn(user)
                }

                networkState = NetworkState.IDLE
                state = SignUpState.SUCCESS
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
        state = SignUpState.FAILURE
    }
}

enum class SignUpState {
    FAILURE,
    SUCCESS,
    NONE,
}

class SignUpActivityViewModelFactory(var listener: ViewModelListener?) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SignUpActivityViewModel(listener) as T
    }
}
