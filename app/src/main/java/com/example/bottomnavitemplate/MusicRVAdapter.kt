package com.example.bottomnavitemplate

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomnavitemplate.databinding.ItemMusicBinding


class MusicRVAdapter() : RecyclerView.Adapter<MusicRVAdapter.ViewHolder>() {

    private val songs = ArrayList<Song>()

    // 삭제 인터페이스 정의
    interface MyItemClickListener{
        fun onRemoveMusic(songId: Int)
    }

    // 외부 리스너 객체를 전달받는 함수랑 외부 리스너 객체를 저장할 변수
    private  lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    // 뷰홀더 생성
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MusicRVAdapter.ViewHolder {
        val binding : ItemMusicBinding = ItemMusicBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    // 뷰홀더에 데이터를 바인딩
    override fun onBindViewHolder(holder: MusicRVAdapter.ViewHolder, position: Int) {
        holder.bind(songs[position])
        holder.binding.itemMusicPlayerMoreIv.setOnClickListener {
            mItemClickListener.onRemoveMusic(songs[position].id)
            removeSong(position)
             }
    }

    // 데이터 세트의 사이즈
    override fun getItemCount(): Int = songs.size

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)

        notifyDataSetChanged()
    }

    fun removeSong(position: Int){
        songs.removeAt(position)
        notifyDataSetChanged()
    }

    // 뷰홀더
    inner class ViewHolder(val binding: ItemMusicBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(music: Song) {
            binding.itemMusicTitleTv.text = music.title
            binding.itemMusicSingerTv.text = music.singer
            binding.itemMusicIv.setImageResource(music.coverImg!!)

        }
    }
}