package com.omplayer.app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.omplayer.app.db.entities.Track
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.fragments.GenreFragment
import com.omplayer.app.repositories.TrackRepository
import com.omplayer.app.utils.LibraryUtil
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class GenreViewModel(application: Application) : AndroidViewModel(application) {
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.IO
    private val scope = CoroutineScope(coroutineContext)
    private val db = SingletonHolder.db

    private val trackRepository: TrackRepository = TrackRepository(db.trackDao())
    private var tracks = ArrayList<Track>()

    fun loadGenreTracks(genreId: Int, fragment:GenreFragment) {
        scope.launch {
            withContext(coroutineContext) {
                tracks = trackRepository.getTracksByGenreId(genreId) as ArrayList<Track>
                withContext(Dispatchers.Main) {
                    LibraryUtil.selectedGenreTracklist = tracks
                    fragment.selectGenre()
                }
            }
        }
    }

    fun getGenreName():String{
        return LibraryUtil.genres[LibraryUtil.selectedGenre].name
    }
}