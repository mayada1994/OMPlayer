package com.omplayer.app.viewmodels

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.omplayer.app.adapters.GenreAdapter
import com.omplayer.app.db.entities.Track
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.repositories.TrackRepository
import com.omplayer.app.utils.LibraryUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GenreViewModel(application: Application) : BaseViewModel(application), GenreAdapter.Callback {

    val genreAdapter = GenreAdapter(LibraryUtil.genres, this)
    private val db = SingletonHolder.db

    private var _viewLiveData: MutableLiveData<View> = MutableLiveData()
    var viewLiveData = _viewLiveData


    private val trackRepository: TrackRepository = TrackRepository(db.trackDao())
    private var tracks = ArrayList<Track>()


    override fun loadGenreTracks(genreId: Int, view: View) {
        launch {
            withContext(coroutineContext) {
                tracks = trackRepository.getTracksByGenreId(genreId) as ArrayList<Track>
                withContext(Dispatchers.Main) {
                    LibraryUtil.selectedGenreTracklist = tracks
                    _viewLiveData.value = view
                }
            }
        }
    }

}