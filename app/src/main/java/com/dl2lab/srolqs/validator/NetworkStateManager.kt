package com.dl2lab.srolqs.validator

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object NetworkStateManager {
    private val _networkState = MutableStateFlow(ConnectivityObserver.Status.Available)
    val networkState: StateFlow<ConnectivityObserver.Status> = _networkState

    fun updateNetworkState(status: ConnectivityObserver.Status) {
        _networkState.value = status
    }
}
