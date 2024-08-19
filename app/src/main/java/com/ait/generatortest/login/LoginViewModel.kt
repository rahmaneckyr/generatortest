package com.ait.generatortest.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel() : BaseViewModel<LoginState, LoginEvent, LoginIntent>(LoginState()) {

    override fun onIntent(intent: LoginIntent) {
        // Handle intents here
    }
}
