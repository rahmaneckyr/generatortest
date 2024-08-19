package com.ait.generatortest.presentation.baka

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class bakaViewModel() : BaseViewModel<bakaState, bakaEvent, bakaIntent>(bakaState()) {

    override fun onIntent(intent: bakaIntent) {
        // Handle intents here
    }
}
