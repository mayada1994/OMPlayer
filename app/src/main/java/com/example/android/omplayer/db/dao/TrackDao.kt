package com.example.android.omplayer.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.omplayer.db.entities.Track

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(track: Track)

    @Update
    fun update(track: Track)

    @Delete
    fun delete(track: Track)

    @Query("SELECT * from tracks ORDER BY title ASC")
    fun getAllTracks(): LiveData<List<Track>>

    @Query("SELECT * from tracks WHERE id = :trackId")
    fun getTrackById(trackId: Int): Track

    @Query("SELECT * from tracks WHERE album_id = :albumId ORDER BY position ASC")
    fun getTracksByAlbumId(albumId: Int): LiveData<List<Track>>

    @Query("SELECT * from tracks WHERE genre_id = :genreId ORDER BY title ASC")
    fun getTracksByGenreId(genreId: Int): LiveData<List<Track>>
}