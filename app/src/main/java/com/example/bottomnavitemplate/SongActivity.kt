package com.example.bottomnavitemplate

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bottomnavitemplate.databinding.ActivityMainBinding
import com.example.bottomnavitemplate.databinding.ActivitySongBinding


class SongActivity : AppCompatActivity(){

    private lateinit var binding: ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            binding.songTitleTv.text = intent.getStringExtra("title")
            binding.songSingerTv.text = intent.getStringExtra("singer")
        }



        binding.songDownIb.setOnClickListener {
            finish()
        }

        binding.songPlayIv.setOnClickListener {
           setPlayerStatus(false)
        }

        binding.songPauseIv.setOnClickListener {
           setPlayerStatus(true)
        }

        binding.songRepeatIv.setOnClickListener {
            binding.songRepeatChangeIv.visibility = View.VISIBLE
            binding.songRepeatIv.visibility = View.GONE
            Toast.makeText(this,"전체 음악을 반복합니다",Toast.LENGTH_SHORT).show()
        }

        binding.songRepeatChangeIv.setOnClickListener {
            binding.songRepeatChangeIv.visibility = View.GONE
            binding.songRepeatChange2Iv.visibility = View.VISIBLE
            Toast.makeText(this,"현재 음악을 반복합니다",Toast.LENGTH_SHORT).show()
        }

        binding.songRepeatChange2Iv.setOnClickListener {
            binding.songRepeatChange2Iv.visibility = View.GONE
            binding.songRepeatIv.visibility = View.VISIBLE
            Toast.makeText(this,"반복을 사용하지 않습니다",Toast.LENGTH_SHORT).show()
        }



        binding.songRandomIv.setOnClickListener {
            setRandomStatus(true)
        }

        binding.songRandomChangeIv.setOnClickListener {
           setRandomStatus(false)

        }


        binding.songLikeIv.setOnClickListener {
            setLikeStatus(true)
        }

        binding.songLikeOnIv.setOnClickListener {
            setLikeStatus(false)
        }

        binding.songUnlikeIv.setOnClickListener {
            setUnlikeStatus(true)
        }

        binding.songUnlikeOnIv.setOnClickListener {
            setUnlikeStatus(false)
        }

    }

    private fun setPlayerStatus(isPlaying : Boolean) {
        if (isPlaying) {
            binding.songPlayIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
        } else{
            binding.songPlayIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        }
    }

    private fun setRandomStatus(isRandom : Boolean){
        if(isRandom){
            binding.songRandomChangeIv.visibility = View.VISIBLE
            binding.songRandomIv.visibility = View.GONE
            Toast.makeText(this,"재생목록을 랜덤으로 재생합니다",Toast.LENGTH_SHORT).show()
        }else{
            binding.songRandomChangeIv.visibility = View.GONE
            binding.songRandomIv.visibility = View.VISIBLE
            Toast.makeText(this,"재생목록을 순서대로 재생합니다",Toast.LENGTH_SHORT).show()
        }
    }

    private fun setLikeStatus(isLike : Boolean) {
        if (isLike) {
            binding.songLikeIv.visibility = View.GONE
            binding.songLikeOnIv.visibility = View.VISIBLE
        }
        else{
            binding.songLikeIv.visibility = View.VISIBLE
            binding.songLikeOnIv.visibility = View.GONE
        }
    }

    private fun setUnlikeStatus(isUnlike : Boolean) {
        if (isUnlike) {
            binding.songUnlikeIv.visibility = View.GONE
            binding.songUnlikeOnIv.visibility = View.VISIBLE
        }
        else{
            binding.songUnlikeIv.visibility = View.VISIBLE
            binding.songUnlikeOnIv.visibility = View.GONE
        }
    }


}


