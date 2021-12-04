package com.example.bottomnavitemplate

import android.content.Context
import androidx.appcompat.app.AppCompatActivity


 fun saveJwt(context: Context, jwt: String){
    val spf = context.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
    val editor = spf.edit()

    editor.putString("jwt",jwt)
    editor.apply()
}

 fun saveUserIdx(context: Context, userIdx: Int){
    val spf = context.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
    val editor = spf.edit()

    editor.putInt("userIdx", userIdx)
    editor.apply()
}

fun saveUserName(context: Context, userName: String){
    val spf = context.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
    val editor = spf.edit()

    editor.putString("userName", userName)
    editor.apply()
}

fun getJwt(context: Context): String?{
    val spf = context.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
    return spf!!.getString("jwt",null)
}


fun getUserIdx(context: Context): Int {
    val spf = context.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)

    return spf!!.getInt("userIdx",0)
}

fun getUserName(context: Context): String? {
    val spf = context.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)

    return spf!!.getString("userName",null)
}




