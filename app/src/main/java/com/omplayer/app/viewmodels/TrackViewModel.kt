package com.omplayer.app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.omplayer.app.fragments.PlayerFragment

class TrackViewModel(application: Application, val fragment: PlayerFragment?) : AndroidViewModel(application) {

    private val lastFmViewModel = LastFmViewModel(application)

    fun loadSimilarTracks(title: String, artist: String, fragment: PlayerFragment){
        lastFmViewModel.getSimilarTracks(title, artist, fragment)
    }

    fun goToFragment(){
        fragment!!.openSimilarTracks()
    }
}