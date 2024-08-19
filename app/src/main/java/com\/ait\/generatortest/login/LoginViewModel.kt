package com.ait.generatortest.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    var loginState: LoginState = LoginState()
        private set

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.Submit -> {
                // Handle submission
            }
            LoginEvent.Reset -> {
                // Handle reset
            }
        }
    }
}
