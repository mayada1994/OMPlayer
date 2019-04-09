package com.omplayer.app.viewmodels

import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.*
import com.omplayer.app.adapters.FavoritesAdapter
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.di.SingletonHolder.application
import com.omplayer.app.di.SingletonHolder.db
import com.omplayer.app.repositories.TrackRepository
import com.omplayer.app.utils.LibraryUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FavoritesViewModel(application: Application) : AndroidViewModel(application), FavoritesAdapter.Callback {

    private val trackRepository: TrackRepository = TrackRepository(db.trackDao())
    private val _viewLiveData: MutableLiveData<View> = MutableLiveData()
    val itemAdapter = FavoritesAdapter(this)
    val viewLiveData: LiveData<View> = _viewLiveData


    override fun openPlayer(view: View) {

        _viewLiveData.value = view

    }

    //TODO Fix on Resume favorites update
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        CoroutineScope(Dispatchers.IO).launch {
            LibraryUtil.favorites = trackRepository.getTracksByFavorite(true)
        }
    }

}