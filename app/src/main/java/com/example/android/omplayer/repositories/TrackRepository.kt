package com.example.android.omplayer.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.android.omplayer.db.PlayerDatabase
import com.example.android.omplayer.db.entities.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrackRepository(val context: Context) {

    private val TAG: String = this.javaClass.simpleName
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    fun insertTrack(track: Track) {
        try {
            scope.launch {
                PlayerDatabase.getDatabase(context).trackDao().insert(track)
            }
        } catch (e: Exception) {
            Log.d(TAG, e.message)
        }
    }

    fun updateTrack(track: Track) {
        try {
            scope.launch {
                PlayerDatabase.getDatabase(context).trackDao().update(track)
            }
        } catch (e: Exception) {
            Log.d(TAG, e.message)
        }
    }

    fun deleteTrack(track: Track) {
        try {
            scope.launch {
                PlayerDatabase.getDatabase(context).trackDao().delete(track)
            }
        } catch (e: Exception) {
            Log.d(TAG, e.message)
        }
    }

    fun getAllTracks(): LiveData<List<Track>>? {
        var tracks: LiveData<List<Track>>? = null
        try {
            scope.launch {
                tracks = withContext(scope.coroutineContext) {
                    PlayerDatabase.getDatabase(context).trackDao().getAllTracks()
                }
            }

        } catch (e: Exception) {
            Log.d(TAG, e.message)
        }
        return tracks
    }

    fun getTrackById(id: Int): Track? {
        var track: Track? = null
        try {
            scope.launch {
                track = withContext(scope.coroutineContext) {
                    PlayerDatabase.getDatabase(context).trackDao().getTrackById(id)
                }
            }

        } catch (e: Exception) {
            Log.d(TAG, e.message)
        }
        return track
    }

    fun getTracksByAlbumId(id: Int): LiveData<List<Track>>? {
        var tracks: LiveData<List<Track>>? = null
        try {
            scope.launch {
                tracks = withContext(scope.coroutineContext) {
                    PlayerDatabase.getDatabase(context).trackDao().getTracksByAlbumId(id)
                }
            }

        } catch (e: Exception) {
            Log.d(TAG, e.message)
        }
        return tracks
    }
}