package com.example.bottomnavitemplate

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.bottomnavitemplate.databinding.ActivitySplashBinding

class SplashActivity: AppCompatActivity(){

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler(Looper.getMainLooper()).postDelayed({
               val intent = Intent(this,MainActivity::class.java)
               startActivity(intent)
               this.finish()
        },2000) // 1초가 1000Millis
      //  autoLogin()
    }

//    private fun autoLogin(){
//
//        val jwt = getJwt(this)!!
//
//        val authService = AuthService()
//        authService.setAutoLoginView(this)
//        authService.autoLogin(jwt)
//    }
//
//    override fun onAutoLoginLoading() {
//    }
//
//    override fun onAutoLoginSuccess() {
//        val intent = Intent(this,MainActivity::class.java)
//        startActivity(intent)
//    }
//
//    override fun onAutoLoginFailure(code: Int) {
//        when(code) {
//           2002,2001 -> startLoginActivity()
//        }
//    }
//
//    private fun startLoginActivity(){
//        val intent = Intent(this,LoginActivity::class.java)
//        startActivity(intent)
//    }
}