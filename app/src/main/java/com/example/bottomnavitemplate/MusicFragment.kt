package com.example.bottomnavitemplate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bottomnavitemplate.databinding.FragmentMusicBinding

class MusicFragment: Fragment() {
     lateinit var binding : FragmentMusicBinding
     lateinit var songDB: SongDatabase
     // 뮤직 데이터
     private var musicDatas = ArrayList<Music>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMusicBinding.inflate(inflater,container,false)
        songDB = SongDatabase.getInstance(requireContext())!!
        // 뮤직 데이터 리스트 생성(더미 데이터)
//        musicDatas.apply {
//            add(Music("LILAC","아이유(IU)", R.drawable.img_album_exp2))
//            add(Music("Butter","방탄소년단(BTS)", R.drawable.img_album_exp))
//            add(Music("Loser","빅뱅(BigBang)", R.drawable.img_album_exp3))
//            add(Music("LILAC","아이유(IU)", R.drawable.img_album_exp2))
//            add(Music("Butter","방탄소년단(BTS)", R.drawable.img_album_exp))
//            add(Music("Loser","빅뱅(BigBang)", R.drawable.img_album_exp3))
//            add(Music("LILAC","아이유(IU)", R.drawable.img_album_exp2))
//            add(Music("Butter","방탄소년단(BTS)", R.drawable.img_album_exp))
//            add(Music("Loser","빅뱅(BigBang)", R.drawable.img_album_exp3))
//            add(Music("LILAC","아이유(IU)", R.drawable.img_album_exp2))
//            add(Music("Butter","방탄소년단(BTS)", R.drawable.img_album_exp))
//            add(Music("Loser","빅뱅(BigBang)", R.drawable.img_album_exp3))
//            add(Music("LILAC","아이유(IU)", R.drawable.img_album_exp2))
//            add(Music("Butter","방탄소년단(BTS)", R.drawable.img_album_exp))
//            add(Music("Loser","빅뱅(BigBang)", R.drawable.img_album_exp3))
//        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecylerview()
    }

    private fun initRecylerview(){

        binding.musicSavedRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        // 리사이클러뷰에 어뎁터 연결

        val musicRVAdapter = MusicRVAdapter()

        musicRVAdapter.setMyItemClickListener(object : MusicRVAdapter.MyItemClickListener{
            override fun onRemoveMusic(songId: Int) {
                songDB.SongDao().updateIsLikeById(false,songId)
            }
        })

        binding.musicSavedRecyclerView.adapter = musicRVAdapter
        musicRVAdapter.addSongs(songDB.SongDao().getLikedSongs(true) as ArrayList)

    }

}