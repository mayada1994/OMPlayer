package com.omplayer.app.viewmodels

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.omplayer.app.adapters.TrackAdapter
import com.omplayer.app.fragments.PlayerFragment
import com.omplayer.app.utils.LibraryUtil

class TrackViewModel(application: Application) : AndroidViewModel(application), TrackAdapter.Callback {

    private val lastFmViewModel = LastFmViewModel(application)

    val itemAdapter = TrackAdapter(LibraryUtil.tracks, this)

    private var _viewLiveData: MutableLiveData<View> = MutableLiveData()
    var viewLiveData = _viewLiveData

    override fun openPlayer(view: View) {
        _viewLiveData.value = view
    }

    fun loadSimilarTracks(title: String, artist: String, fragment: PlayerFragment){
        lastFmViewModel.getSimilarTracks(title, artist, fragment)
    }

    fun goToFragment(fragment: PlayerFragment){
        fragment.openSimilarTracks()
    }
}