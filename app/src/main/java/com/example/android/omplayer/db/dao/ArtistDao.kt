package com.example.android.omplayer.db.dao

import androidx.room.*
import com.example.android.omplayer.db.entities.Artist

@Dao
interface ArtistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(artist: Artist)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg artist: Artist)

    @Update
    fun update(artist: Artist)

    @Delete
    fun delete(artist: Artist)

    @Query("DELETE from artists")
    fun deleteAll()

    @Query("SELECT * from artists ORDER BY name ASC")
    fun getAllArtists(): List<Artist>

    @Query("SELECT * from artists WHERE id = :artistId")
    fun getArtistById(artistId: Int): Artist

    @Query("SELECT * from artists WHERE name = :artistName")
    fun getArtistByName(artistName: String): Artist
}