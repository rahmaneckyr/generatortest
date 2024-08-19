package com.ait.generatortest.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    var homeState: HomeState = HomeState()
        private set

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.Submit -> {
                // Handle submission
            }
            HomeEvent.Reset -> {
                // Handle reset
            }
        }
    }
}
