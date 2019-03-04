package com.example.android.omplayer.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.android.omplayer.db.entities.Artist

interface ArtistDao {
    @Insert
    fun insert(artist: Artist)

    @Update
    fun update(artist: Artist)

    @Delete
    fun delete(artist: Artist)

    @Query("SELECT * from artists ORDER BY id ASC")
    fun getAllArtists(): LiveData<List<Artist>>

    @Query("SELECT * from genres WHERE id = :artistId")
    fun getArtistById(artistId: Int): Artist
}