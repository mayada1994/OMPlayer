package com.example.android.omplayer.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.android.omplayer.db.entities.Artist
import com.example.android.omplayer.repositories.ArtistRepository

class ArtistViewModel(val context: Context) {

    private val artistRepository: ArtistRepository = ArtistRepository(context)

    fun getArtistsFromDb(): LiveData<List<Artist>> {
        return artistRepository.getAllArtists()!!
    }
}