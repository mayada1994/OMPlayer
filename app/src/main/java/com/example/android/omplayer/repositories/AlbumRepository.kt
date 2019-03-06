package com.example.android.omplayer.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.android.omplayer.db.PlayerDatabase
import com.example.android.omplayer.db.entities.Album
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumRepository(val context: Context) {

    private val TAG: String = this.javaClass.simpleName
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    fun insertAlbum(album: Album) {
        try {
            scope.launch {
                PlayerDatabase.getDatabase(context).albumDao().insert(album)
            }
        } catch (e: Exception) {
            Log.d(TAG, e.message)
        }
    }

    fun updateAlbum(album: Album) {
        try {
            scope.launch {
                PlayerDatabase.getDatabase(context).albumDao().update(album)
            }
        } catch (e: Exception) {
            Log.d(TAG, e.message)
        }
    }

    fun deleteAlbum(album: Album) {
        try {
            scope.launch {
                PlayerDatabase.getDatabase(context).albumDao().delete(album)
            }
        } catch (e: Exception) {
            Log.d(TAG, e.message)
        }
    }

    fun getAllAlbums(): LiveData<List<Album>>? {
        var albums: LiveData<List<Album>>? = null
        try {
            scope.launch {
                albums = withContext(scope.coroutineContext) {
                    PlayerDatabase.getDatabase(context).albumDao().getAllAlbums()
                }
            }

        } catch (e: Exception) {
            Log.d(TAG, e.message)
        }
        return albums
    }

    fun getAlbumById(id: Int): Album? {
        var album: Album? = null
        try {
            scope.launch {
                album = withContext(scope.coroutineContext) {
                    PlayerDatabase.getDatabase(context).albumDao().getAlbumById(id)
                }
            }

        } catch (e: Exception) {
            Log.d(TAG, e.message)
        }
        return album
    }

    fun getAlbumsByGenreId(id: Int): LiveData<List<Album>>? {
        var albums: LiveData<List<Album>>? = null
        try {
            scope.launch {
                albums = withContext(scope.coroutineContext) {
                    PlayerDatabase.getDatabase(context).albumDao().getAlbumsByGenreId(id)
                }
            }

        } catch (e: Exception) {
            Log.d(TAG, e.message)
        }
        return albums
    }

    fun getAlbumsByArtistId(id: Int): LiveData<List<Album>>? {
        var albums: LiveData<List<Album>>? = null
        try {
            scope.launch {
                albums = withContext(scope.coroutineContext) {
                    PlayerDatabase.getDatabase(context).albumDao().getAlbumsByArtistId(id)
                }
            }

        } catch (e: Exception) {
            Log.d(TAG, e.message)
        }
        return albums
    }
}