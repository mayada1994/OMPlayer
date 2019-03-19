package com.example.android.omplayer.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.android.omplayer.db.entities.Track
import com.example.android.omplayer.di.SingletonHolder
import com.example.android.omplayer.fragments.GenreFragment
import com.example.android.omplayer.repositories.TrackRepository
import com.example.android.omplayer.utils.LibraryUtil
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
                    //progressBar.visibility = View.GONE
                    fragment.selectGenre()
                }
            }
        }
    }

    fun getGenreName():String{
        return LibraryUtil.genres[LibraryUtil.selectedGenre].name
    }
}