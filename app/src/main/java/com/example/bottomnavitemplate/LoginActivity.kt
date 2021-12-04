package com.example.bottomnavitemplate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bottomnavitemplate.databinding.ActivityLoginBinding

class LoginActivity: AppCompatActivity(), LoginView {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSignUpTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.loginLoginBtn.setOnClickListener {
            login()
          //  startMainActivity()
        }
    }

//    private fun login() {
//        if(binding.loginIdEt.text.toString().isEmpty() || binding.loginDirectInputEt.text.toString().isEmpty()){
//            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        if(binding.loginPasswordEt.text.toString().isEmpty()){
//            Toast.makeText(this, "비밀번호가 입력해주세요.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val email: String = binding.loginIdEt.text.toString() + "@" + binding.loginDirectInputEt.text.toString()
//        val pwd: String = binding.loginPasswordEt.text.toString()
//
//
//        //데이터베이스에 이런정보들이 있는 지 확인
//        val songDB = SongDatabase.getInstance(this)!!
//
//        val user = songDB.UserDao().getUsers(email,pwd)
//
//        user?.let{
//            Log.d("LOGINACT/GET_USER","userId: ${user.id},$user")
//            // 발급받은 jwt를 저장해주는 함수
//            saveJwt(user.id)
//        }
//
//        if(user?.email != email || user.password != pwd) {
//            Toast.makeText(this, "회원정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
//        }
//
//        if(user?.email == email && user.password == pwd) {
//            startMainActivity()
//            Toast.makeText(this, "환영합니다!!!", Toast.LENGTH_SHORT).show()
//        }
//
//    }

    // 네트워킹(API)으로 데이터베이스 저장

    private fun login(){

//        if(binding.loginIdEt.text.toString().isEmpty() || binding.loginDirectInputEt.text.toString().isEmpty()){
//            binding.loginIdErrorTv.visibility = View.VISIBLE
//            binding.loginIdErrorTv.text = "이메일을 입력해주세요."
//        }


        val email: String = binding.loginIdEt.text.toString() + "@" + binding.loginDirectInputEt.text.toString()
        val pwd: String = binding.loginPasswordEt.text.toString()
        val user = User(email,pwd,"")

        val authService = AuthService()
        authService.setLoginView(this)

        authService.login(user)

    }

    private fun startMainActivity(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }


    override fun onLoginLoading() {
       binding.loginLoadingPb.visibility = View.VISIBLE
    }

    override fun onLoginSuccess(auth: Auth) {

        binding.loginLoadingPb.visibility = View.GONE

        saveJwt(this,auth.jwt)
        // roomDB 데이터와 충돌이 안나게 하기 위해...
        saveUserIdx(this,auth.userIdx)
        Toast.makeText(this, "환영합니다!!",Toast.LENGTH_SHORT).show()
        startMainActivity()

        Log.d("LOGIN-SUCCESS",auth.toString())

    }

    override fun onLoginFailure(code: Int, message: String) {
        binding.loginLoadingPb.visibility = View.GONE


        when(code){
            2019,3014 -> {
                binding.loginErrorTv.visibility = View.VISIBLE
                binding.loginErrorTv.text = message
            }
        }

        Log.d("LOGIN_FAILURE",code.toString())
    }

    override fun onLoginMiss(code: Int, message: String) {
        binding.loginLoadingPb.visibility = View.GONE

        when(code) {
            2015 -> {
                binding.loginIdErrorTv.visibility = View.VISIBLE
                binding.loginIdErrorTv.text = message
            }
        }
    }

}