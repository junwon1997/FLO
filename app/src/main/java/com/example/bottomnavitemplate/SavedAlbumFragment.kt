package com.example.bottomnavitemplate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bottomnavitemplate.databinding.FragmentSavedAlbumBinding

class SavedAlbumFragment : Fragment() {

    lateinit var binding: FragmentSavedAlbumBinding
    lateinit var albumDB : SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSavedAlbumBinding.inflate(inflater,container,false)
        albumDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview(){
        binding.savedAlbumSavedRecyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        val albumRVAdapter = AlbumLockerRVAdapter()

        albumRVAdapter.setMyItemClickListener(object : AlbumLockerRVAdapter.MyItemClickListener{
            override fun onRemoveSong(songId: Int) {
                albumDB.AlbumDao().disLikeAlbum(getUserIdx(requireContext()),songId)
                albumDB.AlbumDao().getLikeAlbums(getUserIdx(requireContext()))
            }
        })

        binding.savedAlbumSavedRecyclerView.adapter = albumRVAdapter
        albumRVAdapter.addAlbums(albumDB.AlbumDao().getLikeAlbums(getUserIdx(requireContext())) as ArrayList)

    }

}