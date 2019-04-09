package com.omplayer.app.repositories

import androidx.annotation.WorkerThread
import com.omplayer.app.db.dao.ScrobbledTrackDao
import com.omplayer.app.db.entities.ScrobbledTrack

class ScrobbledTrackRepository(private val scrobbledTrackDao: ScrobbledTrackDao) {

    @WorkerThread
    suspend fun insertTrack(scrobbledTrack: ScrobbledTrack) {
        scrobbledTrackDao.insert(scrobbledTrack)
    }

    @WorkerThread
    suspend fun deleteTrack(scrobbledTrack: ScrobbledTrack) {
        scrobbledTrackDao.delete(scrobbledTrack)
    }

    @WorkerThread
    suspend fun getAllScrobbledTracks(): List<ScrobbledTrack>? {
        return scrobbledTrackDao.getAllScrobbledTracks()
    }

    @WorkerThread
    suspend fun getScrobbledTrackById(id: Int): ScrobbledTrack? {
        return scrobbledTrackDao.getScrobbledTrackById(id)
    }

}