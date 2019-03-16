package com.example.android.omplayer.db.dao

import androidx.room.*
import com.example.android.omplayer.db.entities.Genre

@Dao
interface GenreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(genre: Genre)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg genre: Genre)

    @Update
    fun update(genre: Genre)

    @Delete
    fun delete(genre: Genre)

    @Query("DELETE from genres")
    fun deleteAll()

    @Query("SELECT * from genres ORDER BY name ASC")
    fun getAllGenres(): List<Genre>

    @Query("SELECT * from genres WHERE id = :genreId")
    fun getGenreById(genreId: Int): Genre

    @Query("SELECT * from genres WHERE name = :genreName")
    fun getGenreByName(genreName: String): Genre
}