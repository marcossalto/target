package com.marcossalto.targetmvd.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.marcossalto.targetmvd.network.managers.IUserManager
import com.marcossalto.targetmvd.network.managers.UserManager
import com.marcossalto.targetmvd.ui.base.BaseViewModel
import com.marcossalto.targetmvd.util.NetworkState
import com.marcossalto.targetmvd.util.ViewModelListener
import com.marcossalto.targetmvd.util.extensions.ApiErrorType
import com.marcossalto.targetmvd.util.extensions.ApiException
import kotlinx.coroutines.launch

open class ProfileActivityViewModel(listener: ViewModelListener?) : BaseViewModel(listener) {

    private val manager: IUserManager = UserManager

    fun signOut() {
        networkState = NetworkState.LOADING
        viewModelScope.launch {
            val result = manager.signOut()
            if (result.isSuccess) {
                networkState = NetworkState.IDLE
                state = ProfileState.SUCCESS
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
        state = ProfileState.FAILURE
    }

    var state: ProfileState = ProfileState.NONE
        set(value) {
            field = value
            listener?.updateState()
        }
}

enum class ProfileState {
    FAILURE,
    SUCCESS,
    NONE,
}

class ProfileActivityViewModelFactory(var listener: ViewModelListener?) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileActivityViewModel(listener) as T
    }
}

