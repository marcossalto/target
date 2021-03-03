package com.marcossalto.targetmvd.ui.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.marcossalto.targetmvd.bus
import com.marcossalto.targetmvd.util.NetworkState
import com.marcossalto.targetmvd.util.ViewModelListener
import com.marcossalto.targetmvd.util.extensions.ApiErrorType
import com.marcossalto.targetmvd.util.extensions.ApiException

/**
 * A [ViewModel] base class
 * implement app general LiveData as Session or User
 * **/
open class BaseViewModel(var listener: ViewModelListener?) : ViewModel(), LifecycleObserver {
    var error: String? = null

    var networkState: NetworkState = NetworkState.IDLE
        set(value) {
            field = value
            listener?.updateNetworkState()
        }

    fun getErrorMessageFromException(exception: Throwable?): String? {
        return if (exception is ApiException && exception.errorType == ApiErrorType.API_ERROR) {
            exception.message
        } else null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun register() = bus.register(this)

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun unregister() = bus.unregister(this)
}