package com.omplayer.app.db.dao

import androidx.room.*
import com.omplayer.app.db.entities.Track

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(track: Track)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg track: Track)

    @Update
    fun update(track: Track)

    @Delete
    fun delete(track: Track)

    @Query("DELETE from tracks")
    fun deleteAll()

    @Query("SELECT * from tracks ORDER BY title ASC")
    fun getAllTracks(): List<Track>

    @Query("SELECT * from tracks WHERE id = :trackId")
    fun getTrackById(trackId: Int): Track

    @Query("SELECT * from tracks WHERE album_id = :albumId ORDER BY position ASC")
    fun getTracksByAlbumId(albumId: Int): List<Track>

    @Query("SELECT * from tracks WHERE artist_id = :artistId ORDER BY position ASC")
    fun getTracksByArtistId(artistId: Int): List<Track>

    @Query("SELECT * from tracks WHERE genre_id = :genreId ORDER BY title ASC")
    fun getTracksByGenreId(genreId: Int): List<Track>

    @Query("SELECT * from tracks WHERE favorite = :favStatus ORDER BY title ASC")
    fun getTracksByFavorite(favStatus : Boolean) : List<Track>

    @Query("SELECT COUNT(*) from tracks WHERE artist_id = :artistId")
    fun getArtistTrackNumber(artistId: Int): Int
}