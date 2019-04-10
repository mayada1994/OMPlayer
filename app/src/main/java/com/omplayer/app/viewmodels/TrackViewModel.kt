package com.omplayer.app.viewmodels

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.omplayer.app.adapters.TrackAdapter
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.stateMachine.Action
import com.omplayer.app.utils.LibraryUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrackViewModel(application: Application) : BaseViewModel(application), TrackAdapter.Callback {

    val itemAdapter = TrackAdapter(callback = this)

    private var _viewLiveData: MutableLiveData<View> = MutableLiveData()
    var viewLiveData = _viewLiveData

    init {
        launch {
            val items = LibraryUtil.tracks.map {
                TrackAdapter.Item(it, SingletonHolder.db.artistDao().getArtistById(it.artistId))
            }
            withContext(Dispatchers.Main) {
                itemAdapter.items = items
            }
        }
    }

    override fun openPlayer(position : Int, view: View) {
        LibraryUtil.tracklist = LibraryUtil.tracks
        LibraryUtil.selectedTrack = position
        LibraryUtil.action = Action.Play()
        _viewLiveData.value = view
    }


}