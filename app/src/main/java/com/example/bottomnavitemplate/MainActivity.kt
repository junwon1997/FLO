package com.example.bottomnavitemplate

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.bottomnavitemplate.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private lateinit var  mainplayer: MainPlayer
    private val handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val song = Song(binding.mainMiniplayerTitleTv.text.toString(),binding.mainMiniplayerSingerTv.text.toString(),215,false)

        mainplayer = MainPlayer(song.playTime,song.isPlaying)
        mainplayer.start()

        binding.mainPlayerLayout.setOnClickListener {
           // startActivity(Intent(this,SongActivity::class.java))
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title",song.title)
            intent.putExtra("singer",song.singer)
            intent.putExtra("playTime",song.playTime)
            intent.putExtra("isPlaying",song.isPlaying)
            startActivity(intent)
        }



        binding.mainMiniplayerBtn.setOnClickListener {
            setPlayerStatus(true)
            mainplayer.isPlaying = true
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("isPlaying",true)

        }

        binding.mainPauseBtn.setOnClickListener {
            setPlayerStatus(false)
            mainplayer.isPlaying = false
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("isNotPlaying",false)
        }

        initNavigation()

        binding.mainBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

            }
            false
        }

    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

    }

    private fun setPlayerStatus(isPlaying : Boolean){
        if(isPlaying){
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
        }else{
            binding.mainPauseBtn.visibility = View.GONE
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
        }
    }

    inner class MainPlayer(private val playTime:Int,var isPlaying: Boolean) : Thread() {

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
                            binding.mainPlayerseekbarSb.progress = second * 1000 / playTime
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
        mainplayer.interrupt()
    }




}

