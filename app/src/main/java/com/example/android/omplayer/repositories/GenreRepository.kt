package com.example.android.omplayer.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.android.omplayer.db.PlayerDatabase
import com.example.android.omplayer.db.entities.Genre
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GenreRepository(val context: Context) {

    private val TAG: String = this.javaClass.simpleName
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    fun insertGenre(genre: Genre) {
        try {
            scope.launch {
                PlayerDatabase.getDatabase(context).genreDao().insert(genre)
            }
        } catch (e: Exception) {
            Log.d(TAG, e.message)
        }
    }

    fun updateGenre(genre: Genre) {
        try {
            scope.launch {
                PlayerDatabase.getDatabase(context).genreDao().update(genre)
            }
        } catch (e: Exception) {
            Log.d(TAG, e.message)
        }
    }

    fun deleteGenre(genre: Genre) {
        try {
            scope.launch {
                PlayerDatabase.getDatabase(context).genreDao().delete(genre)
            }
        } catch (e: Exception) {
            Log.d(TAG, e.message)
        }
    }

    fun getAllGenres(): LiveData<List<Genre>>? {
        var genres: LiveData<List<Genre>>? = null
        try {
            scope.launch {
                genres = withContext(scope.coroutineContext) {
                    PlayerDatabase.getDatabase(context).genreDao().getAllGenres()
                }
            }

        } catch (e: Exception) {
            Log.d(TAG, e.message)
        }
        return genres
    }

    fun getGenre(id: Int): Genre? {
        var genre: Genre? = null
        try {
            scope.launch {
                genre = withContext(scope.coroutineContext) {
                    PlayerDatabase.getDatabase(context).genreDao().getGenreById(id)
                }
            }

        } catch (e: Exception) {
            Log.d(TAG, e.message)
        }
        return genre
    }
}