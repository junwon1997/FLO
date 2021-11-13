package com.example.bottomnavitemplate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bottomnavitemplate.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class AlbumFragment : Fragment () {

    lateinit var binding: FragmentAlbumBinding
    private var gson: Gson = Gson()

    private val information = arrayListOf("수록곡","상세정보","영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        //Home 에서 넘어온 데이터 받아오기
        val albumData = arguments?.getString("album")
        val album = gson.fromJson(albumData,Album::class.java)

        //Home 에서 넘어온 데이터 반영
        setInit(album)


        binding.albumBackIb.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

        binding.albumLikeIb.setOnClickListener {
            setLikeStatus(true)
        }

        binding.albumLikeOnIb.setOnClickListener {
           setLikeStatus(false)
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
    }

    private fun setLikeStatus(isLike : Boolean){
        if(isLike){
            binding.albumLikeIb.visibility = View.GONE
            binding.albumLikeOnIb.visibility = View.VISIBLE
        }
        else{
            binding.albumLikeIb.visibility = View.VISIBLE
            binding.albumLikeOnIb.visibility = View.GONE
        }



    }


}