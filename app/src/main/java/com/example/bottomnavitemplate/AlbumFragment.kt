package com.example.bottomnavitemplate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bottomnavitemplate.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class AlbumFragment : Fragment () {

    lateinit var binding: FragmentAlbumBinding
    private var gson: Gson = Gson()

    private val information = arrayListOf("수록곡","상세정보","영상")

    private var isLiked: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        //Home 에서 넘어온 데이터 받아오기
        val albumData = arguments?.getString("album")
        val album = gson.fromJson(albumData,Album::class.java)
        isLiked = isLikedAlbum(album.id)

        //Home 에서 넘어온 데이터 반영
        setInit(album)
        setClickListener(album)


        binding.albumBackIb.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }



        val slidePanel = binding.mainFrame
        slidePanel.anchorPoint = 0.8f

        val albumAdapter = AlbumViewpagerAdapter(this)
        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumContentTb,binding.albumContentVp){
            tab,position ->
            tab.text = information[position]
        }.attach()


        return binding.root
    }

    private fun setInit(album: Album) {
        binding.albumImgIv.setImageResource(album.coverImg!!)
        binding.albumSingerTv.text = album.singer.toString()
        binding.albumTitleTv.text = album.title.toString()

        if(isLiked){
            binding.albumLikeIb.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.albumLikeIb.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun setClickListener(album: Album){
        val userId: Int = getUserIdx(requireContext())

        binding.albumLikeIb.setOnClickListener {
            if(isLiked){
                binding.albumLikeIb.setImageResource(R.drawable.ic_my_like_off)
                disLikedAlbum(userId,album.id)
            }else{
                binding.albumLikeIb.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId,album.id)
            }
        }
    }


    private fun likeAlbum(userId: Int, albumId: Int){
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = Like(userId,albumId)

        songDB.AlbumDao().likeAlbum((like))
    }

    private fun isLikedAlbum(albumId: Int):Boolean{
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getUserIdx(requireContext())

        val likeId: Int? = songDB.AlbumDao().isLikeAlbum(userId,albumId)

        //likeId != null
        return likeId != null
    }

    private fun disLikedAlbum(userId: Int,albumId: Int){
        val songDB = SongDatabase.getInstance(requireContext())!!
        songDB.AlbumDao().isLikeAlbum(userId,albumId)

    }

}