package com.example.android.omplayer.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.omplayer.db.entities.Genre

@Dao
interface GenreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(genre: Genre)

    @Update
    fun update(genre: Genre)

    @Delete
    fun delete(genre: Genre)

    @Query("SELECT * from genres ORDER BY name ASC")
    fun getAllGenres(): LiveData<List<Genre>>

    @Query("SELECT * from genres WHERE id = :genreId")
    fun getGenreById(genreId: Int): Genre
}