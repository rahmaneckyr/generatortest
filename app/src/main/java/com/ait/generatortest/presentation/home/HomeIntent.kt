package com.ait.generatortest.presentation.home

interface HomeIntent {
    fun submitData(username: String, password: String)
    fun resetForm()
}
