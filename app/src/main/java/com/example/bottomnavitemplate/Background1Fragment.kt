package com.example.bottomnavitemplate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bottomnavitemplate.databinding.FragmentBackground1Binding

class Background1Fragment : Fragment(){

    lateinit var binding: FragmentBackground1Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBackground1Binding.inflate(inflater,container,false)

        return binding.root
    }
}