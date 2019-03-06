package com.example.android.omplayer.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.android.omplayer.db.PlayerDatabase
import com.example.android.omplayer.db.entities.Artist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArtistRepository(val context: Context) {

    private val TAG: String = this.javaClass.simpleName
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    fun insertArtist(artist: Artist) {
        try {
            scope.launch {
                PlayerDatabase.getDatabase(context).artistDao().insert(artist)
            }
        } catch (e: Exception) {
            Log.d(TAG, e.message)
        }
    }

    fun updateArtist(artist: Artist) {
        try {
            scope.launch {
                PlayerDatabase.getDatabase(context).artistDao().update(artist)
            }
        } catch (e: Exception) {
            Log.d(TAG, e.message)
        }
    }

    fun deleteArtist(artist: Artist) {
        try {
            scope.launch {
                PlayerDatabase.getDatabase(context).artistDao().delete(artist)
            }
        } catch (e: Exception) {
            Log.d(TAG, e.message)
        }
    }

    fun getAllArtists(): LiveData<List<Artist>>? {
        var artists: LiveData<List<Artist>>? = null
        try {
            scope.launch {
                artists = withContext(scope.coroutineContext) {
                    PlayerDatabase.getDatabase(context).artistDao().getAllArtists()
                }
            }

        } catch (e: Exception) {
            Log.d(TAG, e.message)
        }
        return artists
    }

    fun getArtist(id: Int): Artist? {
        var artist: Artist? = null
        try {
            scope.launch {
                artist = withContext(scope.coroutineContext) {
                    PlayerDatabase.getDatabase(context).artistDao().getArtistById(id)
                }
            }

        } catch (e: Exception) {
            Log.d(TAG, e.message)
        }
        return artist
    }
}