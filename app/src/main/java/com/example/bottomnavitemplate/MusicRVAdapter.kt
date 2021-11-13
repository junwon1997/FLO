package com.example.bottomnavitemplate

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomnavitemplate.databinding.ItemMusicBinding


class MusicRVAdapter(private var musicList: ArrayList<Music>) : RecyclerView.Adapter<MusicRVAdapter.ViewHolder>() {

    // 삭제 인터페이스 정의
    interface MyItemClickListener{
        fun onRemoveMusic(position: Int)
    }

    // 외부 리스너 객체를 전달받는 함수랑 외부 리스너 객체를 저장할 변수
    private  lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    fun removeItem(position: Int){
        musicList.removeAt(position)
        notifyDataSetChanged()
    }

    // 뷰홀더 생성
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MusicRVAdapter.ViewHolder {
        val binding : ItemMusicBinding = ItemMusicBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    // 뷰홀더에 데이터를 바인딩
    override fun onBindViewHolder(holder: MusicRVAdapter.ViewHolder, position: Int) {
        holder.bind(musicList[position])
        holder.binding.itemMusicPlayerMoreIv.setOnClickListener { mItemClickListener.onRemoveMusic(position) }
    }

    // 데이터 세트의 사이즈
    override fun getItemCount(): Int = musicList.size


    // 뷰홀더
    inner class ViewHolder(val binding: ItemMusicBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(music: Music) {
            binding.itemMusicTitleTv.text = music.title
            binding.itemMusicSingerTv.text = music.singer
            binding.itemMusicIv.setImageResource(music.coverImg!!)

        }
    }
}