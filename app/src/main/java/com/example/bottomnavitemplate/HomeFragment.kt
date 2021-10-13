package com.example.bottomnavitemplate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.bottomnavitemplate.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeImgMiniAlbum1Iv.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, AlbumFragment())
                .commitAllowingStateLoss()
        }

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


}