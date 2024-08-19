package com.ait.generatortest.login

interface LoginIntent {
    fun submitData(username: String, password: String)
    fun resetForm()
}
