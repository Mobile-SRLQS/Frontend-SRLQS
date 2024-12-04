package com.dl2lab.srolqs.validator

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object NetworkStateManager {
    private val _networkState = MutableStateFlow<ConnectivityObserver.Status>(ConnectivityObserver.Status.Unavailable)
    val networkState = _networkState.asStateFlow()

    fun updateNetworkState(status: ConnectivityObserver.Status) {
        _networkState.value = status
    }
}
