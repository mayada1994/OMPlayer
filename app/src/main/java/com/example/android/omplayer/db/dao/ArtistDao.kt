package com.example.android.omplayer.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.omplayer.db.entities.Artist

@Dao
interface ArtistDao {
    @Insert
    fun insert(artist: Artist)

    @Update
    fun update(artist: Artist)

    @Delete
    fun delete(artist: Artist)

    @Query("SELECT * from artists ORDER BY name ASC")
    fun getAllArtists(): LiveData<List<Artist>>

    @Query("SELECT * from artists WHERE id = :artistId")
    fun getArtistById(artistId: Int): Artist
}