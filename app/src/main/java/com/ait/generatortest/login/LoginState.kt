package com.ait.generatortest.login

data class LoginState(
    val username: String = ,
    val password: String = ,
    val isLoading: Boolean = false,
    val error: String? = null
)
