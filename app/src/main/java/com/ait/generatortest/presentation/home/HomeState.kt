package com.ait.generatortest.presentation.home

data class HomeState(
    val username: String = ,
    val password: String = ,
    val isLoading: Boolean = false,
    val error: String? = null
)
