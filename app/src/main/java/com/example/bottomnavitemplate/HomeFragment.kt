package com.example.bottomnavitemplate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.bottomnavitemplate.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson


class HomeFragment() : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private var albumDatas = ArrayList<Album>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentHomeBinding.inflate(inflater, container, false)

//        binding.homeImgMiniAlbum1Iv.setOnClickListener{
//            (context as MainActivity).supportFragmentManager.beginTransaction()
//                .replace(R.id.main_frm, AlbumFragment())
//                .commitAllowingStateLoss()
//        }

        // 데이터 리스트 생성 ( 더미데이터 )
        albumDatas.apply {
            add(Album("LILAC","아이유(IU)", R.drawable.img_album_exp2))
            add(Album("Butter","방탄소년단(BTS)", R.drawable.img_album_exp))
            add(Album("Loser","빅뱅(BigBang)", R.drawable.img_album_exp3))
            add(Album("LILAC","아이유(IU)", R.drawable.img_album_exp2))
            add(Album("Butter","방탄소년단(BTS)", R.drawable.img_album_exp))
            add(Album("Loser","빅뱅(BigBang)", R.drawable.img_album_exp3))
            add(Album("LILAC","아이유(IU)", R.drawable.img_album_exp2))
            add(Album("Butter","방탄소년단(BTS)", R.drawable.img_album_exp))
            add(Album("Loser","빅뱅(BigBang)", R.drawable.img_album_exp3))
            add(Album("LILAC","아이유(IU)", R.drawable.img_album_exp2))
            add(Album("Butter","방탄소년단(BTS)", R.drawable.img_album_exp))
            add(Album("Loser","빅뱅(BigBang)", R.drawable.img_album_exp3))
            add(Album("LILAC","아이유(IU)", R.drawable.img_album_exp2))
            add(Album("Butter","방탄소년단(BTS)", R.drawable.img_album_exp))
            add(Album("Loser","빅뱅(BigBang)", R.drawable.img_album_exp3))
         }



        // 어뎁터 설정(더미데이터와 어뎁터 연결)
        val albumRVAdapter = AlbumRVAdapter(albumDatas)

        // 리사이클러뷰에 어뎁터를 연결
        binding.homeTodayMusicAlbumRecyclerView.adapter = albumRVAdapter

        albumRVAdapter.setMyItemClickListener(object  : AlbumRVAdapter.MyItemClickListener{

            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }
        })

        // 레이아웃 매니저 설정
        binding.homeTodayMusicAlbumRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)

        val bannerAdapter = BannerViewpagerAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))


        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val homebackgroundAdapter = HomeBackgroundAdapter(this)
        binding.homeBackgroundVp.adapter = homebackgroundAdapter
        binding.homeBackgroundVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        TabLayoutMediator(binding.homeBackgroundTb,binding.homeBackgroundVp)
        { tab,position ->}.attach()
        return binding.root
    }

    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
    }


}