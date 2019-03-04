package com.example.android.omplayer.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.android.omplayer.db.entities.Album

interface AlbumDao {
    @Insert
    fun insert(album: Album)

    @Update
    fun update(album: Album)

    @Delete
    fun delete(album: Album)

    @Query("SELECT * from albums ORDER BY id ASC")
    fun getAllAlbums(): LiveData<List<Album>>

    @Query("SELECT * from albums WHERE id = :albumId")
    fun getAlbumById(albumId: Int): Album

    @Query("SELECT * from albums WHERE genre_id = :genreId")
    fun getAlbumByGenreId(genreId: Int): Album

    @Query("SELECT * from albums WHERE artist_id = :artistId")
    fun getAlbumByArtistId(artistId: Int): Album
}