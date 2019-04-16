package com.omplayer.app.viewmodels

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import com.omplayer.app.adapters.TrackAdapter
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.di.SingletonHolder.db
import com.omplayer.app.repositories.TrackRepository
import com.omplayer.app.stateMachine.Action
import com.omplayer.app.utils.LibraryUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FavoritesViewModel(application: Application) : BaseViewModel(application),
    TrackAdapter.Callback,
    LifecycleObserver {

    private val trackRepository: TrackRepository = TrackRepository(db.trackDao())

    private val _viewLiveData: MutableLiveData<View> = MutableLiveData()
    val viewLiveData: LiveData<View> = _viewLiveData

    val itemAdapter = TrackAdapter(callback = this)

    init {
        launch {
            val items = trackRepository.getTracksByFavorite(true).map {
                TrackAdapter.Item(it, SingletonHolder.db.artistDao().getArtistById(it.artistId))
            }
            withContext(Dispatchers.Main) {
                itemAdapter.items = items
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        launch {
            val favorites = trackRepository.getTracksByFavorite(true)
            val items = trackRepository.getTracksByFavorite(true).map {
                TrackAdapter.Item(it, SingletonHolder.db.artistDao().getArtistById(it.artistId))

            }
            withContext(Dispatchers.Main) {
                itemAdapter.items = items
                LibraryUtil.favorites = favorites
            }
        }

    }

    override fun openPlayer(position: Int, view: View) {
        LibraryUtil.tracklist = LibraryUtil.favorites
        LibraryUtil.selectedTrack = position
        LibraryUtil.action = Action.Play()
        _viewLiveData.value = view
    }

}

