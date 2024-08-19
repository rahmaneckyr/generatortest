package com.ait.generatortest.login

sealed class LoginEvent {
    data class Submit(val username: String, val password: String) : LoginEvent()
    object Reset : LoginEvent()
}
