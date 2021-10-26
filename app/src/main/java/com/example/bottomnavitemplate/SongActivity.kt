package com.example.bottomnavitemplate

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bottomnavitemplate.databinding.ActivityMainBinding
import com.example.bottomnavitemplate.databinding.ActivitySongBinding


class SongActivity : AppCompatActivity(){

    private lateinit var binding: ActivitySongBinding

    private val song: Song = Song()
    private lateinit var  player:Player
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if(intent.hasExtra("title") && intent.hasExtra("singer") && intent.hasExtra("playTime") && intent.hasExtra("isPlaying")){
            binding.songTitleTv.text = intent.getStringExtra("title")
            binding.songSingerTv.text = intent.getStringExtra("singer")
            song.playTime = intent.getIntExtra("playTime",0)
            song.isPlaying = intent.getBooleanExtra("isPlaying",false)
            binding.songTotaltimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
            setPlayerStatus(song.isPlaying)
        }

        player = Player(song.playTime,song.isPlaying)
        player.start()


        binding.songDownIb.setOnClickListener {
            finish()
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
            player.isPlaying = false
        }

        binding.songPlayIv.setOnClickListener {
            setPlayerStatus(true)
            player.isPlaying = true
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
            binding.songPlayIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        } else{
            binding.songPlayIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
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

    inner class Player(private val playTime:Int,var isPlaying: Boolean) : Thread() {

        private var second = 0

        override fun run() {
            try{
                while (true) {
                    if(second >= playTime){
                        break
                    }
                    if(isPlaying){
                        sleep(1000)
                        second++

                        handler.post {
                            binding.songPlayerseekbarSb.progress = second * 1000 / playTime
                            binding.songPlaytimeTv.text = String.format("%02d:%02d", second / 60, second % 60)
                        }
                    }
                }
            }catch (e : InterruptedException){
                Log.d("interrupt","쓰레드가 종료되었습니다")
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.interrupt()
    }

}


