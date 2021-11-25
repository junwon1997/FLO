package com.example.bottomnavitemplate

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bottomnavitemplate.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator


class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding


    private val information = arrayListOf("저장한 곡","음악파일","저장앨범")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        val lockerAdapter = LockerViewpagerAdapter(this)
        binding.lockerContentVp.adapter = lockerAdapter
        TabLayoutMediator(binding.lockerContentTb,binding.lockerContentVp){
                tab,position ->
            tab.text = information[position]
        }.attach()

        binding.lockerLoginTv.setOnClickListener {
            startActivity(Intent(activity,LoginActivity::class.java))
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initView()
    }

    private fun initView() {
        val jwt = getJwt()
        val userDB = SongDatabase.getInstance(requireContext())!!
        val userName = userDB.UserDao().getUsersName(jwt)

        if(jwt == 0){
            binding.lockerLoginTv.text = "로그인"

            binding.lockerLoginTv.setOnClickListener {
                startActivity(Intent(activity,LoginActivity::class.java))
            }
        }
        else {

            binding.lockerLoginTv.text = "로그아웃"
            binding.lockerUserNameTv.text = userName?.name

            binding.lockerLoginTv.setOnClickListener {
                logout()
                Toast.makeText(requireContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(activity,MainActivity::class.java))
            }
        }
    }

    // jwt를 가져오는 함수
    private fun getJwt(): Int {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)

        return spf!!.getInt("jwt",0)
    }

    // 로그아웃을 시켜주는 함수
    private fun logout(){
        val spf = activity?.getSharedPreferences("auth",AppCompatActivity.MODE_PRIVATE)
        val editor = spf!!.edit()

        editor.remove("jwt")
        editor.apply()
    }

}