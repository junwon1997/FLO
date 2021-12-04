package com.example.bottomnavitemplate

import retrofit2.Call
import retrofit2.http.*

interface AuthRetrofitInterface {
    @POST("/users")
    fun signUp(@Body user: User): Call<AuthResponse>

    @POST("/users/login")
    fun login(@Body user: User): Call<AuthResponse>

    @GET("/users/auto-login")
    fun autologin(@Header("jwt") jwt: String): Call<AuthResponse>
}