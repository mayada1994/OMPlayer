package com.example.android.omplayer.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.android.omplayer.db.entities.Track
import com.example.android.omplayer.repositories.TrackRepository

class TrackViewModel(val context: Context) {
    val trackRepository: TrackRepository = TrackRepository(context)

    fun getTracksFromDb(): LiveData<List<Track>> {
        return trackRepository.getAllTracks()!!
    }
}