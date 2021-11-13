package com.example.bottomnavitemplate

import android.content.res.Resources
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.bottomnavitemplate.databinding.ActivityMainBinding
import com.example.bottomnavitemplate.databinding.ActivitySongBinding
import com.google.gson.Gson


class SongActivity : AppCompatActivity(){

    private lateinit var binding: ActivitySongBinding

    private var song: Song = Song()
    private lateinit var  player:Player
    private val handler = Handler(Looper.getMainLooper())

    private var mediaPlayer: MediaPlayer? = null

    private var gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("song_onCreate", "hi")
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initSong()
//
//        player = Player(song.playTime,song.isPlaying,song.second)
//        player.start()


//        binding.songPlayerseekbarSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                    Log.d("ProgressChanged","hi")
//                    song.second = (seekBar!!.progress * song.playTime) / 1000
//                    binding.songPlaytimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
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
//                  binding.songPlaytimeTv.text = String.format("%02d:%02d",song.second/60,song.second%60)
//            }
//
//        })


        binding.songDownIb.setOnClickListener {
           finish()
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
            player.isPlaying = false
            song.isPlaying = false
            mediaPlayer?.pause()
        }

        binding.songPlayIv.setOnClickListener {
            setPlayerStatus(true)
            player.isPlaying = true
            song.isPlaying = true
            mediaPlayer?.start()
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

    private fun initSong() {
        if (intent.hasExtra("title") && intent.hasExtra("singer") && intent.hasExtra("second") && intent.hasExtra(
                "playTime"
            ) && intent.hasExtra("isPlaying") && intent.hasExtra("music")
        ) {
            song.title = intent.getStringExtra("title")!!
            song.singer = intent.getStringExtra("singer")!!
            song.second = intent.getIntExtra("second", 0)
            song.playTime = intent.getIntExtra("playTime", 0)
            song.isPlaying = intent.getBooleanExtra("isPlaying", false)
            song.music = intent.getStringExtra("music")!!
            val music = resources.getIdentifier(song.music, "raw", this.packageName)

            binding.songTitleTv.text = intent.getStringExtra("title")
            binding.songSingerTv.text = intent.getStringExtra("singer")
            binding.songTotaltimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
            setPlayerStatus(song.isPlaying)
            mediaPlayer = MediaPlayer.create(this, music)
        }
    }

    private fun setPlayerStatus(isPlaying: Boolean) {
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

    inner class Player(private val playTime:Int,var isPlaying: Boolean,private var second : Int) : Thread() {



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



    override fun onPause() {
        super.onPause()
        Log.d("song_OnPause","hi")

       if(binding.songPlayIv.isVisible){
           song.isPlaying = false
           mediaPlayer?.pause()
       }
       else{
           song.isPlaying = true
           mediaPlayer?.pause()
       }

        player.isPlaying = false // 스레드 정지
        song.second = (binding.songPlayerseekbarSb.progress * song.playTime)/1000

        //sharedPreferences
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() //sharedPreferences 조작할 때 사용을 한다
        // Gson
        val json = gson.toJson(song)
        editor.putString("song",json)

        editor.apply()
    }

    override fun onStart() {
        super.onStart()
        Log.d("song_start","hi")
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val jsonSong = sharedPreferences.getString("song",null)
        song = if(jsonSong == null){
            Song("Loser","빅뱅(BigBang)",0,226,false,"music_loser")
        }else{
            gson.fromJson(jsonSong, Song::class.java)
        }

        binding.songTitleTv.text = song.title
        binding.songSingerTv.text = song.singer
        setPlayerStatus(song.isPlaying)
        binding.songPlayerseekbarSb.progress = (song.second*1000/song.playTime)
        binding.songPlaytimeTv.text = String.format("%02d:%02d", song.second / 60 , song.second % 60)

        mediaPlayer?.seekTo(song.second * 1000)

        player = Player(song.playTime,song.isPlaying,song.second)
        player.start()

        if(song.isPlaying){
            mediaPlayer?.start()
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d("song_OnDestroy","hi")
        player.interrupt() // 스레드 해제
        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해지
    }

}


