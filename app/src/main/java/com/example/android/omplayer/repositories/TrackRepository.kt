package com.example.android.omplayer.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.android.omplayer.db.dao.TrackDao
import com.example.android.omplayer.db.entities.Track

class TrackRepository(private val trackDao: TrackDao) {

    @WorkerThread
    suspend fun insertTrack(track: Track) {
        trackDao.insert(track)
    }

    @WorkerThread
    suspend fun insertAllTracks(track: ArrayList<Track>) {
        trackDao.insertAll(*track.toTypedArray())
    }

    @WorkerThread
    suspend fun updateTrack(track: Track) {
        trackDao.update(track)
    }

    @WorkerThread
    suspend fun deleteTrack(track: Track) {
        trackDao.delete(track)
    }

    @WorkerThread
    suspend fun deleteAllTracks() {
        try {
            trackDao.deleteAll()
        } catch (e: Exception) {
        }
    }

    @WorkerThread
    suspend fun getAllTracks(): List<Track>? {
        return trackDao.getAllTracks()
    }

    @WorkerThread
    suspend fun getTrackById(id: Int): Track? {
        return trackDao.getTrackById(id)
    }

    @WorkerThread
    suspend fun getTracksByAlbumId(id: Int): List<Track>? {
        return trackDao.getTracksByAlbumId(id)
    }

    @WorkerThread
    suspend fun getTracksByGenreId(id: Int): List<Track>? {
        return trackDao.getTracksByGenreId(id)
    }
}