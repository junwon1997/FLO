package com.example.bottomnavitemplate

interface AutoLoginView {
    fun onAutoLoginLoading()
    fun onAutoLoginSuccess()
    fun onAutoLoginFailure(code: Int)
}