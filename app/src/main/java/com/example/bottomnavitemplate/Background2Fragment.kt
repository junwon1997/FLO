package com.example.bottomnavitemplate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bottomnavitemplate.databinding.FragmentBackground2Binding

class Background2Fragment : Fragment() {

    lateinit var binding: FragmentBackground2Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBackground2Binding.inflate(inflater,container,false)

        return binding.root
    }
}