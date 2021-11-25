package com.example.bottomnavitemplate

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.bottomnavitemplate.databinding.ActivityMainBinding
import com.google.gson.Gson


class MainActivity : AppCompatActivity(){
    lateinit var binding: ActivityMainBinding
    private var songs =  ArrayList<Song>()
    private var nowPos = 0
    private lateinit var songDB: SongDatabase

    // Gson
    private var gson: Gson = Gson()

    // 미디어 플레이어
    private var mediaPlayer: MediaPlayer? = null

    // 쓰레드
    private lateinit var  mainplayer: MainPlayer
    private val handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("main_onCreate", " hi")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()
        inputDummySongs()
        inputDummyAlbums()

       // val song = Song("Loser","빅뱅(BigBang)",0,226,false,"music_loser")

//        setMiniplayer(song)
//
//        mainplayer = MainPlayer(song.playTime,song.isPlaying,song.second)
//        mainplayer.start()


        binding.mainPlayerLayout.setOnClickListener {
           // startActivity(Intent(this,SongActivity::class.java))
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId",songs[nowPos].id)
            editor.apply()

            val intent = Intent(this@MainActivity, SongActivity::class.java)
            startActivity(intent)


//            intent.putExtra("title",song.title)
//            intent.putExtra("singer",song.singer)
//            intent.putExtra("second",song.second)
//            intent.putExtra("playTime",song.playTime)
//            intent.putExtra("isPlaying",song.isPlaying)
//            intent.putExtra("music",song.music)

        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("main_OnStart","hi")

        val spf = getSharedPreferences("song", MODE_PRIVATE)
//        val jsonSong = sharedPreferences.getString("song",null)
//        song = if(jsonSong == null){
//            Song("Loser","빅뱅(BigBang)",0,226,false,"music_loser")
//        }else{
//            gson.fromJson(jsonSong, Song::class.java)
//        }
        val songId = spf.getInt("songId",0)
        val songDB = SongDatabase.getInstance(this)!!

        nowPos = getPlayingSongPosition(songId)

        songs[nowPos] = if(songId == 0){
             songDB.SongDao().getSong(1)
        }
        else {
            songDB.SongDao().getSong(songId)
        }

        Log.d("song ID",songs[nowPos].id.toString())
        setMiniplayer(songs[nowPos])

        mainplayer = MainPlayer(songs[nowPos].playTime,songs[nowPos].isPlaying,songs[nowPos].second)
        mainplayer.start()

        if(songs[nowPos].isPlaying){
            mediaPlayer?.start()
        }

        binding.mainMiniplayerBtn.setOnClickListener {
            setPlayerStatus(true)
            mainplayer.isPlaying = true
            songs[nowPos].isPlaying = true
            mediaPlayer?.start()

        }

        binding.mainPauseBtn.setOnClickListener {
            setPlayerStatus(false)
            mainplayer.isPlaying = false
            songs[nowPos].isPlaying = false
            mediaPlayer?.pause()

        }

        binding.mainPlayerseekbarSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                songs[nowPos].second = seekBar!!.progress * songs[nowPos].playTime / 1000
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                songs[nowPos].second = seekBar!!.progress * songs[nowPos].playTime / 1000
                if(songs[nowPos].isPlaying) {
                    mainplayer.interrupt()
                    mainplayer = MainPlayer(
                        songs[nowPos].playTime,
                        songs[nowPos].isPlaying,
                        songs[nowPos].second
                    )
                    mainplayer.start()
                }
                else{
                    mainplayer = MainPlayer(
                        songs[nowPos].playTime,
                        songs[nowPos].isPlaying,
                        songs[nowPos].second
                    )
                    mainplayer.start()
                }

                mediaPlayer?.seekTo(songs[nowPos].second * 1000)
            }
        })

        binding.mainMiniplayerNextIv.setOnClickListener {
            moveSong(+1)
        }

        binding.mainMiniplayerPreviousIv.setOnClickListener {
            moveSong(-1)
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("main_OnPause","hi")

        if(binding.mainMiniplayerBtn.isVisible){
            songs[nowPos].isPlaying = false
            mediaPlayer?.pause()
        }
        else{
            songs[nowPos].isPlaying = true
            mediaPlayer?.pause()
        }

        mainplayer.isPlaying = false // 쓰레드 중지
        songs[nowPos].second = (binding.mainPlayerseekbarSb.progress * songs[nowPos].playTime)/1000

        songDB.SongDao().update(songs[nowPos])

        //sharedPreferences
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() //sharedPreferences 조작할 때 사용을 한다
//        // Gson
//        val json = gson.toJson(song)
//        editor.putString("song",json)

        editor.putInt("songId",songs[nowPos].id)
        editor.apply()

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("main_destroy","hi")
        mainplayer.interrupt()
    }

    private fun getPlayingSongPosition(songId: Int): Int{
        for(i in 0 until songs.size){
            if(songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

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

    private fun moveSong(direct: Int){
           if(nowPos + direct < 0){
               Toast.makeText(this,"first song", Toast.LENGTH_SHORT).show()
               return
           }

        if(nowPos + direct >= songs.size ){
            Toast.makeText(this,"last song",Toast.LENGTH_SHORT).show()
            return
        }

        nowPos += direct

        mainplayer.interrupt()
        mainplayer = MainPlayer(songs[nowPos].playTime,songs[nowPos].isPlaying,songs[nowPos].second)
        mainplayer.start()
        mediaPlayer?.release()
        mediaPlayer = null
        setMiniplayer(songs[nowPos])

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
         binding.mainPlayerseekbarSb.progress = (song.second*1000/song.playTime)

        val music = resources.getIdentifier(song.music, "raw",this.packageName)
        mediaPlayer = MediaPlayer.create(this,music)
        mediaPlayer?.seekTo(song.second * 1000)

        setPlayerStatus(song.isPlaying)
    }

    //ROOM_DB
    private fun inputDummyAlbums() {
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.AlbumDao().getAlbums()

        if (albums.isNotEmpty()) return

        songDB.AlbumDao().insert(
            Album(
                1,
                "Loser", "빅뱅 (Loser)", R.drawable.img_album_exp3
            )
        )

        songDB.AlbumDao().insert(
            Album(2,
                "LILAC", "아이유 (IU)", R.drawable.img_album_exp2
            )
        )

        songDB.AlbumDao().insert(
            Album(
                    3,
                "Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp
            )
        )

        songDB.AlbumDao().insert(
            Album(
                4,
                "LILAC", "아이유 (IU)", R.drawable.img_album_exp2
            )
        )

        songDB.AlbumDao().insert(
            Album(
                5,
                "Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp
            )
        )

        songDB.AlbumDao().insert(
            Album(
                   6,
                "Loser", "빅뱅 (Loser)", R.drawable.img_album_exp3
            )
        )


    }


    private fun inputDummySongs(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.SongDao().getSongS())// SongTable 에 있는 데이터 다 가져오기

        if(songs.isNotEmpty()) return

        songDB.SongDao().insert(
            Song(
                "Loser",
                "빅뱅(BigBang)",
                0,
                226,
                false,
                "music_loser",
                R.drawable.img_album_exp3,
                false
            )
        )


        songDB.SongDao().insert(
            Song(
                "라일락",
                "아이유(IU)",
                0,
                200,
                false,
                "music_lilac",
                R.drawable.img_album_exp2,
                false
            )
        )


        songDB.SongDao().insert(
            Song(
                "Butter",
                "방탄소년단(BTS)",
                0,
                190,
                false,
                "music_butter",
                R.drawable.img_album_exp,
                false
            )
        )


        // 데이터 잘 들어갔는지 확인
        val _songs = songDB.SongDao().getSongS()
        Log.d("DB_DATA",_songs.toString())

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

}

