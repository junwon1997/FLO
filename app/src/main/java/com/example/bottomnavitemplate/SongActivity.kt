package com.example.bottomnavitemplate

import android.annotation.SuppressLint
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
import java.util.concurrent.TimeUnit


class SongActivity : AppCompatActivity(){

    private lateinit var binding: ActivitySongBinding

    private lateinit var song: Song

    // RoomDB
    private var songs = ArrayList<Song>()
    private var nowPos = 0
    private lateinit var songDB: SongDatabase

    // 쓰레드
    private val handler = Handler(Looper.getMainLooper())

    // 미디어 플레이어
    private lateinit var  player:Player
    private var mediaPlayer: MediaPlayer? = null

    //Gson
    private var gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("song_onCreate", "hi")
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()
        initSong()
        initClickListener()

    }

    override fun onPause() {
        super.onPause()
        Log.d("song_OnPause","hi")

        if(binding.songPlayIv.isVisible){
            songs[nowPos].isPlaying = false
            mediaPlayer?.pause()
        }
        else{
            songs[nowPos].isPlaying = true
            mediaPlayer?.pause()
        }

        songs[nowPos].second = (songs[nowPos].playTime * binding.songPlayerseekbarSb.progress) / 1000
        setPlayerStatus(false)

        songDB.SongDao().update(songs[nowPos])

        //sharedPreferences
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() //sharedPreferences 조작할 때 사용을 한다
        // Gson
        //val json = gson.toJson(song)
        //editor.putString("song",json)

        editor.putInt("songId",songs[nowPos].id)
        editor.apply()
    }

    override fun onStart() {
        super.onStart()
        Log.d("song_start","hi")
//        val spf = getSharedPreferences("song", MODE_PRIVATE)
//        val jsonSong = sharedPreferences.getString("song",null)
//        song = if(jsonSong == null){
//            Song("Loser","빅뱅(BigBang)",0,226,false,"music_loser")
//        }else{
//            gson.fromJson(jsonSong, Song::class.java)
//        }

//        val spf = getSharedPreferences("song", MODE_PRIVATE)
//        val songId = spf.getInt("songId",0)
//
//        nowPos = 0
//
//        binding.songTitleTv.text = songs[nowPos].title
//        binding.songSingerTv.text = songs[nowPos].singer
//        setPlayerStatus(songs[nowPos].isPlaying)
//        binding.songPlayerseekbarSb.progress = (songs[nowPos].second*1000/songs[nowPos].playTime)
//        binding.songPlaytimeTv.text = String.format("%02d:%02d", songs[nowPos].second / 60 , songs[nowPos].second % 60)
//        mediaPlayer?.seekTo(songs[nowPos].second * 1000)
//
//        player = Player(songs[nowPos].playTime,songs[nowPos].isPlaying,songs[nowPos].second)
//        player.start()
//
//        if(songs[nowPos].isPlaying){
//            mediaPlayer?.start()
//        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("song_OnDestroy","hi")
        player.interrupt() // 스레드 해제
        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해지
    }

    // songs 에 songList 를 추가해주는 함수
    private fun initPlayList(){
         songDB = SongDatabase.getInstance(this)!!
         songs.addAll(songDB.SongDao().getSongS())
    }

    private fun initSong() {
//        if (intent.hasExtra("title") && intent.hasExtra("singer") && intent.hasExtra("second") && intent.hasExtra(
//                "playTime"
//            ) && intent.hasExtra("isPlaying") && intent.hasExtra("music")
//        ) {
//            song.title = intent.getStringExtra("title")!!
//            song.singer = intent.getStringExtra("singer")!!
//            song.second = intent.getIntExtra("second", 0)
//            song.playTime = intent.getIntExtra("playTime", 0)
//            song.isPlaying = intent.getBooleanExtra("isPlaying", false)
//            song.music = intent.getStringExtra("music")!!
//            val music = resources.getIdentifier(song.music, "raw", this.packageName)
//
//            binding.songTitleTv.text = intent.getStringExtra("title")
//            binding.songSingerTv.text = intent.getStringExtra("singer")
//            binding.songTotaltimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
//            setPlayerStatus(song.isPlaying)
//            mediaPlayer = MediaPlayer.create(this, music)
//        }

        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId",0)

        nowPos = getPlayingSongPosition(songId)

        player = Player(songs[nowPos].playTime,songs[nowPos].isPlaying,
            songs[nowPos].second.toLong()
        )
        player.start()

        setPlayer(songs[nowPos])

        if(songs[nowPos].isPlaying){
            mediaPlayer?.start()
        }
    }

    private fun getPlayingSongPosition(songId: Int): Int{
        for(i in 0 until songs.size){
            if(songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    private fun setPlayer(song: Song) {
        val music = resources.getIdentifier(song.music, "raw", this.packageName)

        binding.songTitleTv.text = song.title
        binding.songSingerTv.text = song.singer
        binding.songPlayerseekbarSb.progress = (song.second * 1000 / song.playTime)
        binding.songPlaytimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songTotaltimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songAlbumIv.setImageResource(song.coverImg!!)

        setPlayerStatus(song.isPlaying)

        if(song.isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

       mediaPlayer = MediaPlayer.create(this, music)
        mediaPlayer?.seekTo(song.second * 1000)
    }

    private fun setLike(isLike: Boolean){
        songs[nowPos].isLike = !isLike
        songDB.SongDao().updateIsLikeById(!isLike,songs[nowPos].id)


        if(isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }

    }


    private fun moveSong(direct: Int){
        if(nowPos + direct < 0){
            Toast.makeText(this,"first song",Toast.LENGTH_SHORT).show()
            return
        }
        if(nowPos + direct >= songs.size){
            Toast.makeText(this,"last song",Toast.LENGTH_SHORT).show()
            return
        }

        nowPos += direct

        player.interrupt()
        player = Player(songs[nowPos].playTime,songs[nowPos].isPlaying,
            songs[nowPos].second.toLong()
        )
        player.start()
        mediaPlayer?.release()
        mediaPlayer = null
        setPlayer(songs[nowPos])

    }


    private fun initClickListener() {
        binding.songSkip2Iv.setOnClickListener {
               moveSong(+1)
        }

        binding.songSkip1Iv.setOnClickListener {
             moveSong(-1)
        }

        binding.songDownIb.setOnClickListener {
            finish()
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
            player.isPlaying = false
            songs[nowPos].isPlaying = false
            mediaPlayer?.pause()
        }

        binding.songPlayIv.setOnClickListener {
            setPlayerStatus(true)
            player.isPlaying = true
            songs[nowPos].isPlaying = true
            mediaPlayer?.start()
        }

        binding.songRepeatIv.setOnClickListener {
            binding.songRepeatChangeIv.visibility = View.VISIBLE
            binding.songRepeatIv.visibility = View.GONE
            Toast.makeText(this, "전체 음악을 반복합니다", Toast.LENGTH_SHORT).show()
        }

        binding.songRepeatChangeIv.setOnClickListener {
            binding.songRepeatChangeIv.visibility = View.GONE
            binding.songRepeatChange2Iv.visibility = View.VISIBLE
            Toast.makeText(this, "현재 음악을 반복합니다", Toast.LENGTH_SHORT).show()
        }

        binding.songRepeatChange2Iv.setOnClickListener {
            binding.songRepeatChange2Iv.visibility = View.GONE
            binding.songRepeatIv.visibility = View.VISIBLE
            Toast.makeText(this, "반복을 사용하지 않습니다", Toast.LENGTH_SHORT).show()
        }



        binding.songRandomIv.setOnClickListener {
            setRandomStatus(true)
        }

        binding.songRandomChangeIv.setOnClickListener {
            setRandomStatus(false)

        }


        binding.songLikeIv.setOnClickListener {
               setLike(songs[nowPos].isLike)
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

    private fun setPlayerStatus(isPlaying: Boolean) {
        player.isPlaying = isPlaying

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

    inner class Player(private val playTime:Int,var isPlaying: Boolean,private var second : Long) : Thread() {

        @SuppressLint("SetTextI18n")
        override fun run() {
            try{
                while (true) {

                    if(second >= playTime){
                        break
                    }
                    if(isPlaying){
                        sleep(1000)
                        second++

                        runOnUiThread {
                            binding.songPlayerseekbarSb.progress =
                                (second * 1000 / playTime).toInt()
                            binding.songPlaytimeTv.text = String.format(
                                "%02d:%02d",
                                TimeUnit.SECONDS.toMinutes(second),
                                second % 60
                            )
                        }
                    }
                }
            }catch (e : InterruptedException){
                Log.d("interrupt","쓰레드가 종료되었습니다")
            }

        }
    }





}


