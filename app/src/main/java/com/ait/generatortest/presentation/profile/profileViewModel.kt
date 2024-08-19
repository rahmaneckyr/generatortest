package com.ait.generatortest.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class profileViewModel() : BaseViewModel<profileState, profileEvent, profileIntent>(profileState()) {

    override fun onIntent(intent: profileIntent) {
        // Handle intents here
    }
}
