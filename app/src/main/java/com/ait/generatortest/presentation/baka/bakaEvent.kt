package com.ait.generatortest.presentation.baka

sealed class bakaEvent {
    data class Submit(val username: String, val password: String) : bakaEvent()
    object Reset : bakaEvent()
}
