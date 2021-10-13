package com.example.bottomnavitemplate

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerViewpagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragmentlist : ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int = fragmentlist.size
    //fragmentlist안 fragment의 개수를 알려주는 함수
    //override fun getItemCount(): Int{
    //  return fragmentlist.size
    //}

    override fun createFragment(position: Int): Fragment = fragmentlist[position]
    // fragmentlist안 fragment을 생성하는 함수

    fun addFragment(fragment: Fragment){ //함수를 통해 fragmentlist 변수 접근
        fragmentlist.add(fragment)
        notifyItemInserted(fragmentlist.size - 1)
        //배열의 인덱스가 0부터 시작하는 속성때문에
    }
}