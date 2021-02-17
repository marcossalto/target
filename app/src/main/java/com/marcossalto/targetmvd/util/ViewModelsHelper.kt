package com.marcossalto.targetmvd.util

interface ViewModelListener {
    fun updateState()
    fun updateNetworkState()
}

enum class NetworkState {
    LOADING,
    IDLE,
    ERROR
}
