package com.ait.generatortest.presentation.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MessageViewModel() : BaseViewModel<MessageState, MessageEvent, MessageIntent>(MessageState()) {

    override fun onIntent(intent: MessageIntent) {
        // Handle intents here
    }
}
