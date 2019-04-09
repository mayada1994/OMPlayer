package com.omplayer.app.db.dao

import androidx.room.*
import com.omplayer.app.db.entities.ScrobbledTrack

@Dao
interface ScrobbledTrackDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(scrobbledTrack: ScrobbledTrack)

    @Delete
    fun delete(scrobbledTrack: ScrobbledTrack)

    @Query("SELECT * from scrobbled ORDER BY title ASC")
    fun getAllScrobbledTracks(): List<ScrobbledTrack>

    @Query("SELECT * from scrobbled WHERE id = :scrobbledTrackId")
    fun getScrobbledTrackById(scrobbledTrackId: Int): ScrobbledTrack
}