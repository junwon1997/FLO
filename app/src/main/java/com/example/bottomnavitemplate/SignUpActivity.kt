package com.example.bottomnavitemplate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bottomnavitemplate.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SignUpActivity : AppCompatActivity(), SignUpView {

    lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpSignUpBtn.setOnClickListener {
            signUp()
        }

    }

    // 사용자의 아이디와 패스워드 가져오는 함수
    private fun getUser(): User {
        val email: String = binding.signUpIdEt.text.toString() + "@" + binding.signUpDirectInputEt.text.toString()
        val pwd: String = binding.signUpPasswordEt.text.toString()
        val name: String = binding.signUpNameEt.text.toString()

        return User(email,pwd,name)
    }

    // 회원가입 함수
    private fun signUp() {
//        if (binding.signUpIdEt.text.toString()
//                .isEmpty() || binding.signUpDirectInputEt.text.toString().isEmpty()
//        ) {
//            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
//            return
//        }

        if (binding.signUpPasswordEt.text.toString().isEmpty() || binding.signUpPasswordCheckEt.text.toString().isEmpty()
        ) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.signUpPasswordEt.text.toString() != binding.signUpPasswordCheckEt.text.toString()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.signUpNameEt.text.toString().isEmpty()) {
            Toast.makeText(this, "사용자 이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

//        // 회원가입이 무사히 완료되었다면, 사용자가 입력한 정보값들을 데이터베이스에 저장
//        val userDB = SongDatabase.getInstance(this)!!
//        userDB.UserDao().insert(getUser())
//
//        val users = userDB.UserDao().getUsers()
//
//        Log.d("SIGNUPACT",users.toString())
//
//        Toast.makeText(this, "회원가입이 완료되었습니다.",Toast.LENGTH_SHORT).show()
//        finish()


        // 네트워크(API)를 통해 데이터베이스 저장
        val authService = AuthService()
        authService.setSignUpView(this)

        authService.signUp(getUser())
    //    saveUserName(this, getUser().name)


        Log.d("SIGNUPACT/ASYNC","hi")
    }

    override fun onSignUpLoading() {
        binding.signUpLoadingPb.visibility = View.VISIBLE
    }

    override fun onSignUpSuccess() {
        binding.signUpLoadingPb.visibility = View.GONE
        Toast.makeText(this, "회원가입이 완료되었습니다.",Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onSignUpFailure(code: Int, message: String) {
        binding.signUpLoadingPb.visibility = View.GONE

        when(code) {
            2016,2017 -> {
                binding.signUpEmailErrorTv.visibility = View.VISIBLE
                binding.signUpEmailErrorTv.text = message
            }
        }

    }

}