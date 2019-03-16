package com.example.android.omplayer.repositories

import androidx.annotation.WorkerThread
import com.example.android.omplayer.db.dao.ArtistDao
import com.example.android.omplayer.db.entities.Artist

class ArtistRepository(private val artistDao: ArtistDao) {

    @WorkerThread
    suspend fun insertArtist(artist: Artist) {
        artistDao.insert(artist)
    }

    @WorkerThread
    suspend fun insertAllArtists(artist: ArrayList<Artist>) {
        artistDao.insertAll(*artist.toTypedArray())
    }

    @WorkerThread
    suspend fun updateArtist(artist: Artist) {
        artistDao.update(artist)
    }

    @WorkerThread
    suspend fun deleteArtist(artist: Artist) {
        artistDao.delete(artist)
    }

    @WorkerThread
    suspend fun deleteAllArtists() {
        try {
            artistDao.deleteAll()
        } catch (e: Exception) {
        }
    }

    @WorkerThread
    suspend fun getAllArtists(): List<Artist>? {
        return artistDao.getAllArtists()
    }

    @WorkerThread
    suspend fun getArtistById(id: Int): Artist? {
        return artistDao.getArtistById(id)
    }

    @WorkerThread
    suspend fun getArtistByName(name: String): Artist? {
        return artistDao.getArtistByName(name)
    }
}