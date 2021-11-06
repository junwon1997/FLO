package com.example.bottomnavitemplate

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.bottomnavitemplate.databinding.ActivityMainBinding
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private var gson: Gson = Gson()

    private var song: Song = Song()

    private lateinit var  mainplayer: MainPlayer

    private var mediaPlayer: MediaPlayer? = null

    private val handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigation()

        val song = Song("라일락","아이유",0,215,false,"music_loser")

        setMiniplayer(song)

        mainplayer = MainPlayer(song.playTime,song.isPlaying)
        mainplayer.start()

        binding.mainPlayerLayout.setOnClickListener {
           // startActivity(Intent(this,SongActivity::class.java))
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title",song.title)
            intent.putExtra("singer",song.singer)
            intent.putExtra("second",song.second)
            intent.putExtra("playTime",song.playTime)
            intent.putExtra("isPlaying",song.isPlaying)
            intent.putExtra("music",song.music)
            startActivity(intent)
        }


        binding.mainMiniplayerBtn.setOnClickListener {
            setPlayerStatus(true)
            mainplayer.isPlaying = true
            song.isPlaying = true
            mediaPlayer?.start()

        }

        binding.mainPauseBtn.setOnClickListener {
            setPlayerStatus(false)
            mainplayer.isPlaying = false
            song.isPlaying = false
            mediaPlayer?.pause()

        }

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
        if (isPlaying) {
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
        } else {
            binding.mainPauseBtn.visibility = View.GONE
            binding.mainMiniplayerBtn.visibility = View.VISIBLE

        }
    }

    private fun setMiniplayer(song: Song){
         binding.mainMiniplayerTitleTv.text = song.title
         binding.mainMiniplayerSingerTv.text = song.singer
         setPlayerStatus(song.isPlaying)
         binding.mainPlayerseekbarSb.progress = (song.second*1000/song.playTime)
         val music = resources.getIdentifier(song.music, "raw",this.packageName)
         mediaPlayer = MediaPlayer.create(this,music)
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

    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val jsonSong = sharedPreferences.getString("song",null)
        song = if(jsonSong == null){
            Song("아이유","아이유",0,215,false,"music_loser")
        }else{
            gson.fromJson(jsonSong, Song::class.java)
        }
       setMiniplayer(song)
    }

}

