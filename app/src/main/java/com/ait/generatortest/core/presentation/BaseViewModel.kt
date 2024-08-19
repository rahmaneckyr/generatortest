package com.ait.generatortest.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class BaseViewModel<STATE : State, EVENT, INTENT>(
    initialState: STATE,
) : ViewModel(), KoinComponent {
    private val loginPrefDataSource: LoginPrefDataSource by inject()

    abstract fun onIntent(intent: INTENT)

    // UI state
    private val _state = MutableStateFlow(initialState)
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = initialState
        )
    val currentState: STATE get() = state.value
    protected fun setState(update: (old: STATE) -> STATE): STATE = _state.updateAndGet(update)

    // UI event
    val eventChannel = Channel<EVENT>()
    val event = eventChannel.receiveAsFlow()

    fun sendEvent(event: EVENT) = viewModelScope.launch {
        eventChannel.send(event)
    }

    // loading state
    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    // connection Error
    private val _connectionState = MutableStateFlow(false)
    val connectionState = _connectionState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    private val _downloadState = MutableStateFlow(false)
    val downloadState = _downloadState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    fun setDownloadCompleted(isShow: Boolean = true) {
        _downloadState.value = isShow
    }

    private fun setConnectionError(isConnected: Boolean = true) {
        _connectionState.value = isConnected
    }

    fun triggerToastConnection() = viewModelScope.launch {
        setConnectionError(true)
        delay(1000)
        setConnectionError(false)
    }

    fun triggerToastDownloadCompleted() = viewModelScope.launch {
        setDownloadCompleted(true)
        delay(3000)
        setDownloadCompleted(false)
    }

    fun setLoading(isLoading: Boolean = true) {
        _loadingState.value = isLoading
    }

    // error state
    private val _responseState = MutableStateFlow(StatusResponse())
    val responseState = _responseState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = StatusResponse()
        )

    fun setStatusResponse(statusResponse: StatusResponse = StatusResponse()) =
        viewModelScope.launch {
            // mapping for handle 401
            if (statusResponse.code == ResponseApi.authExpiredJwt ||
                statusResponse.code == ResponseApi.authInvalidJwt ||
                statusResponse.code == ResponseApi.authUnauthorizedDeviceId)
                loginPrefDataSource.clearLogin(isForceLogout = true)
            // update error state
            _responseState.value = statusResponse
        }
}
