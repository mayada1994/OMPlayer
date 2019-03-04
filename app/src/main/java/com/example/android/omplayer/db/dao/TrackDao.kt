package com.example.android.omplayer.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.android.omplayer.db.entities.Track

interface TrackDao {
    @Insert
    fun insert(track: Track)

    @Update
    fun update(track: Track)

    @Delete
    fun delete(track: Track)

    @Query("SELECT * from tracks ORDER BY id ASC")
    fun getAllTracks(): LiveData<List<Track>>

    @Query("SELECT * from tracks WHERE id = :trackId")
    fun getTrackById(trackId: Int): Track

    @Query("SELECT * from tracks WHERE album_id = :albumId")
    fun getTrackByAlbumId(albumId: Int): Track
}