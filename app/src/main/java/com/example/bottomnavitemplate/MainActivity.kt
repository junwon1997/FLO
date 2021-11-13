package com.example.bottomnavitemplate

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.bottomnavitemplate.databinding.ActivityMainBinding
import com.google.gson.Gson


class MainActivity : AppCompatActivity(){
    lateinit var binding: ActivityMainBinding

    private var gson: Gson = Gson()

    private var song: Song = Song()

    private lateinit var  mainplayer: MainPlayer

    private var mediaPlayer: MediaPlayer? = null

    private val handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("main_onCreate", " hi")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigation()

        val song = Song("Loser","빅뱅(BigBang)",0,226,false,"music_loser")

        setMiniplayer(song)

        mainplayer = MainPlayer(song.playTime,song.isPlaying,song.second)
        mainplayer.start()

//        binding.mainPlayerseekbarSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                    Log.d("ProgressChanged","hi")
//                    song.second = (seekBar!!.progress * song.playTime) / 1000
//
//            }
//            override fun onStartTrackingTouch(seekBar: SeekBar?){
//                Log.d("StartTracking","hi")
//                song.second = (seekBar!!.progress * song.playTime) / 1000
//
//            }
//
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                  Log.d("StopTracking","hi")
//                  song.second = (seekBar!!.progress * song.playTime) / 1000
//                  mediaPlayer?.seekTo(song.second * 1000)
//            }
//
//        })

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
        mediaPlayer?.seekTo(song.second * 1000)
    }

    inner class MainPlayer(private val playTime:Int,var isPlaying: Boolean,private var second : Int) : Thread() {

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



    override fun onStart() {
        super.onStart()
        Log.d("main_OnStart","hi")

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val jsonSong = sharedPreferences.getString("song",null)
        song = if(jsonSong == null){
            Song("Loser","빅뱅(BigBang)",0,226,false,"music_loser")
        }else{
            gson.fromJson(jsonSong, Song::class.java)
        }

        setMiniplayer(song)

        mainplayer = MainPlayer(song.playTime,song.isPlaying,song.second)
        mainplayer.start()

       if(song.isPlaying){
           mediaPlayer?.start()
       }
    }

    override fun onPause() {
        super.onPause()
        Log.d("main_OnPause","hi")

        if(binding.mainMiniplayerBtn.isVisible){
            song.isPlaying = false
            mediaPlayer?.pause()
        }
        else{
            song.isPlaying = true
            mediaPlayer?.pause()
        }

        mainplayer.isPlaying = false // 쓰레드 중지
        song.second = (binding.mainPlayerseekbarSb.progress * song.playTime)/1000

        //sharedPreferences
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() //sharedPreferences 조작할 때 사용을 한다
        // Gson
        val json = gson.toJson(song)
        editor.putString("song",json)

        editor.apply()

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("main_destroy","hi")
        mainplayer.interrupt()
    }

}

