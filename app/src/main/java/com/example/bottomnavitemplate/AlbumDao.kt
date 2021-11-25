package com.example.bottomnavitemplate

import androidx.room.*

@Dao
interface AlbumDao {

    @Insert
    fun insert(albumId: Album)

    @Update
    fun update(albumId: Album)

    @Delete
    fun delete(albumId: Album)

    @Query("SELECT * FROM AlbumTable")
    fun getAlbums(): List<Album>

    @Query("SELECT * FROM AlbumTable WHERE id = :id")
    fun getAlbum(id: Int): Album

    @Insert
    fun likeAlbum(like: Like)

    @Query("SELECT id FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun isLikeAlbum(userId : Int, albumId : Int): Int?

    @Query("DELETE FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun disLikeAlbum(userId : Int, albumId : Int)

    @Query("SELECT AT.* FROM LikeTable as LT LEFT JOIN AlbumTable as AT ON LT.albumId = AT.id WHERE LT.userId =:userId")
    fun getLikeAlbums(userId: Int): List<Album>

}