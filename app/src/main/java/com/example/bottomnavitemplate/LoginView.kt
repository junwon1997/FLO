package com.example.bottomnavitemplate

interface LoginView {
    fun onLoginLoading()
    fun onLoginSuccess(auth: Auth)
    fun onLoginFailure(code: Int, message: String)
    fun onLoginMiss(code: Int, message: String)
}