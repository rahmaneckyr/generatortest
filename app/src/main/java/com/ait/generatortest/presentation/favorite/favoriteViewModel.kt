package com.ait.generatortest.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class favoriteViewModel() : BaseViewModel<favoriteState, favoriteEvent, favoriteIntent>(favoriteState()) {

    override fun onIntent(intent: favoriteIntent) {
        // Handle intents here
    }
}
