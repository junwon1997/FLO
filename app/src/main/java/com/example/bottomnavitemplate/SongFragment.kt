package com.example.bottomnavitemplate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bottomnavitemplate.databinding.FragmentSongBinding

class SongFragment : Fragment() {

    lateinit var binding: FragmentSongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater,container,false)


        binding.albumToggleBoxIv.setOnClickListener {
            setToggleStatus(true)
        }
        binding.albumToggleChangeBoxIv.setOnClickListener {
           setToggleStatus(false)
        }

        binding.albumNumber1Cl.setOnClickListener {
            Toast.makeText(activity,"라일락을 재생합니다", Toast.LENGTH_SHORT).show()
        }
        return binding.root
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