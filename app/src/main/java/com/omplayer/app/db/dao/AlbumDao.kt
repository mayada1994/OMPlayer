package com.omplayer.app.db.dao

import androidx.room.*
import com.omplayer.app.db.entities.Album

@Dao
interface AlbumDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(album: Album)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg album: Album)

    @Update
    fun update(album: Album)

    @Delete
    fun delete(album: Album)

    @Query("DELETE from albums")
    fun deleteAll()

    @Query("SELECT * from albums ORDER BY artist_id, year ASC")
    fun getAllAlbums(): List<Album>

    @Query("SELECT * from albums WHERE id = :albumId")
    fun getAlbumById(albumId: Int): Album

    @Query("SELECT * from albums WHERE title = :albumTitle")
    fun getAlbumByTitle(albumTitle: String): Album

    @Query("SELECT * from albums WHERE artist_id = :artistId ORDER BY year ASC")
    fun getAlbumsByArtistId(artistId: Int): List<Album>

    @Query("SELECT * from albums WHERE artist_id = :artistId AND title = :albumTitle AND year = :albumYear")
    fun getAlbumByTrack(artistId: Int, albumTitle: String, albumYear: String): Album
}