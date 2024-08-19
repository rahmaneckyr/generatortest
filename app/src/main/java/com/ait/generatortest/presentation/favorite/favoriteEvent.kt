package com.ait.generatortest.presentation.favorite

sealed class favoriteEvent {
    data class Submit(val username: String, val password: String) : favoriteEvent()
    object Reset : favoriteEvent()
}
