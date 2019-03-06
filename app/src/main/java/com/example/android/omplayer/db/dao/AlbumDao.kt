package com.example.android.omplayer.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.omplayer.db.entities.Album

@Dao
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

    @Query("SELECT * from albums WHERE genre_id = :genreId ORDER BY artist_id, year ASC")
    fun getAlbumsByGenreId(genreId: Int): LiveData<List<Album>>

    @Query("SELECT * from albums WHERE artist_id = :artistId ORDER BY year ASC")
    fun getAlbumsByArtistId(artistId: Int): LiveData<List<Album>>
}