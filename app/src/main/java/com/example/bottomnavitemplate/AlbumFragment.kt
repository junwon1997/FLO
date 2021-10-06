package com.example.bottomnavitemplate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bottomnavitemplate.databinding.FragmentAlbumBinding
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class AlbumFragment : Fragment () {

    lateinit var binding: FragmentAlbumBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

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

        binding.albumToggleBoxIv.setOnClickListener {
            setToggleStatus(true)
        }
        binding.albumToggleChangeBoxIv.setOnClickListener {
           setToggleStatus(false)
        }

        binding.albumNumber1Cl.setOnClickListener {
            Toast.makeText(activity,"라일락을 재생합니다",Toast.LENGTH_SHORT).show()
        }

        val slidePanel = binding.mainFrame
        slidePanel.anchorPoint = 0.8f


        return binding.root
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

    private fun setToggleStatus(isToggle : Boolean){
        if(isToggle){
            binding.albumToggleChangeBoxIv.visibility = View.VISIBLE
            binding.albumToggleBoxIv.visibility = View.GONE
        }
        else{
            binding.albumToggleChangeBoxIv.visibility = View.GONE
            binding.albumToggleBoxIv.visibility = View.VISIBLE
        }
    }
}