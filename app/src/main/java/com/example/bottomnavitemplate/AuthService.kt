package com.example.bottomnavitemplate

import android.util.Log
import android.view.View
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.sign

class AuthService {
    private lateinit var signUpView: SignUpView
    private lateinit var loginView: LoginView
    private lateinit var autoLoginView: AutoLoginView

    fun setSignUpView(signUpView: SignUpView){
        this.signUpView = signUpView
    }

    fun setLoginView(loginView: LoginView){
        this.loginView = loginView
    }

    fun setAutoLoginView(autoLoginView: AutoLoginView){
        this.autoLoginView = autoLoginView
    }

    fun signUp(user: User){
        val retrofit = Retrofit.Builder().baseUrl("http:/13.125.121.202").addConverterFactory(GsonConverterFactory.create()).build()
        val authService = retrofit.create(AuthRetrofitInterface::class.java)

        signUpView.onSignUpLoading()

        authService.signUp(user).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("SIGNUPACT/API-RESPONSE", response.toString())


                val resp = response.body()!!

                when(resp.code){
                    1000 -> signUpView.onSignUpSuccess()
                    else -> signUpView.onSignUpFailure(resp.code,resp.message)
                }

            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("SIGNUPACT/API-ERROR", t.toString())

                signUpView.onSignUpFailure(400,"네트워크 오류가 발생했습니다.")
            }

        })
    }

    fun login(user: User){
        val retrofit = Retrofit.Builder().baseUrl("http:/13.125.121.202").addConverterFactory(GsonConverterFactory.create()).build()
        val authService = retrofit.create(AuthRetrofitInterface::class.java)

        loginView.onLoginLoading()

        authService.login(user).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("SIGNUPACT/API-RESPONSE", response.toString())


                val resp = response.body()!!

                when(resp.code){
                    1000 -> loginView.onLoginSuccess(resp.result!!)
                    2015 -> loginView.onLoginMiss(resp.code,resp.message)
                    else -> loginView.onLoginFailure(resp.code,resp.message)
                }

            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("SIGNUPACT/API-ERROR", t.toString())

                loginView.onLoginFailure(400,"네트워크 오류가 발생했습니다.")
            }

        })
    }

    fun autoLogin(jwt : String){
        val retrofit = Retrofit.Builder().baseUrl("http:/13.125.121.202").addConverterFactory(GsonConverterFactory.create()).build()
        val authService = retrofit.create(AuthRetrofitInterface::class.java)

        autoLoginView.onAutoLoginLoading()

        authService.autologin(jwt).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("AUTOACT/API-RESPONSE", response.toString())


                val resp = response.body()!!

                when(resp.code){
                    1000 -> autoLoginView.onAutoLoginSuccess()
                    else -> autoLoginView.onAutoLoginFailure(resp.code)
                }

            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("AUTOACT/API-ERROR", t.toString())

                autoLoginView.onAutoLoginFailure(400)
            }

        })
    }



}