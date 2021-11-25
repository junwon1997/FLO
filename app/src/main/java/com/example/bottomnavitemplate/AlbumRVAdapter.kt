package com.example.bottomnavitemplate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomnavitemplate.databinding.ItemAlbumBinding

class AlbumRVAdapter(private val albumList: ArrayList<Album>) : RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

    // 클릭 이벤트 인터페이스 정의
    interface MyItemClickListener{
        fun onItemClick(album: Album)
    }



    // 외부 리스너 객체를 전달받는 함수 , 외부 리스너 객체를 저장할 변수
    private lateinit var mItemClockListener : MyItemClickListener

    fun setMyItemClickListener(itemClickListener : MyItemClickListener){
        mItemClockListener = itemClickListener
    }


    // 뷰홀더를 생성해줘야 할 때 호출되는 함수 -> 아이템 뷰 객체를 만들어서 뷰홀더 던져 준다.
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumRVAdapter.ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }


    // 생성된 뷰홀더에 데이터를 바인딩시킬 때 호출되는 함수
    override fun onBindViewHolder(holder: AlbumRVAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener{ mItemClockListener.onItemClick(albumList[position])}

    }


    // 데이터 세트 크기를 알려주는 함수 -> 리사이클러뷰의 마지막이 언제인지를 알 수 있다.
    override fun getItemCount(): Int = albumList.size

    // 뷰홀더
    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album) {
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumIv.setImageResource(album.coverImg!!)


        }
    }

}