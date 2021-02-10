package com.marcossalto.targetmvd.ui.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.marcossalto.targetmvd.bus
import com.marcossalto.targetmvd.util.NetworkState
import com.marcossalto.targetmvd.util.ViewModelListener

/**
 * A [ViewModel] base class
 * implement app general LiveData as Session or User
 * **/
open class BaseViewModel(var listener: ViewModelListener?) : ViewModel(), LifecycleObserver {
    var error: String? = null

    var networkState: NetworkState = NetworkState.idle
        set(value) {
            field = value
            listener?.updateNetworkState()
        }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun register() = bus.register(this)

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun unregister() = bus.unregister(this)
}