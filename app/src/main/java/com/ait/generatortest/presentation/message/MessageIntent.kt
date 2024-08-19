package com.ait.generatortest.presentation.message

interface MessageIntent {
    fun submitData(username: String, password: String)
    fun resetForm()
}
