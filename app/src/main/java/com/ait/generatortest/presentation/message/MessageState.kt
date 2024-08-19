package com.ait.generatortest.presentation.message

data class MessageState(
    val username: String = ,
    val password: String = ,
    val isLoading: Boolean = false,
    val error: String? = null
)
