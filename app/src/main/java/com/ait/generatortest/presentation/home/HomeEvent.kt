package com.ait.generatortest.presentation.home

sealed class HomeEvent {
    data class Submit(val username: String, val password: String) : HomeEvent()
    object Reset : HomeEvent()
}
