package com.ait.generatortest.presentation.message

sealed class MessageEvent {
    data class Submit(val username: String, val password: String) : MessageEvent()
    object Reset : MessageEvent()
}
