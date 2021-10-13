package com.example.bottomnavitemplate

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeBackgroundAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
                0 -> Background1Fragment()
                1 -> Background2Fragment()
                else -> Background3Fragment()


        }
    }

}