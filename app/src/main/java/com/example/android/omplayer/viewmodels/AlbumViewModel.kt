package com.example.android.omplayer.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.android.omplayer.db.entities.Album
import com.example.android.omplayer.repositories.AlbumRepository

class AlbumViewModel(val context: Context) {

    val albumRepository: AlbumRepository = AlbumRepository(context)

    fun getAlbumsFromDb(): LiveData<List<Album>> {
        return albumRepository.getAllAlbums()!!
    }
}