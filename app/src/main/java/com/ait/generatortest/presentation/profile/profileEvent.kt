package com.ait.generatortest.presentation.profile

sealed class profileEvent {
    data class Submit(val username: String, val password: String) : profileEvent()
    object Reset : profileEvent()
}
