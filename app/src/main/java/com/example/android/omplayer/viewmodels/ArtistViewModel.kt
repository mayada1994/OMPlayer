package com.example.android.omplayer.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.android.omplayer.db.entities.Album
import com.example.android.omplayer.di.SingletonHolder
import com.example.android.omplayer.fragments.ArtistFragment
import com.example.android.omplayer.repositories.AlbumRepository
import com.example.android.omplayer.utils.LibraryUtil
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ArtistViewModel(application: Application) : AndroidViewModel(application) {
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.IO
    private val scope = CoroutineScope(coroutineContext)
    private val db = SingletonHolder.db

    private val albumRepository:AlbumRepository = AlbumRepository(db.albumDao())
    private var albums = ArrayList<Album>()

    fun loadArtistAlbums(artistId: Int, fragment: ArtistFragment) {
        scope.launch {
            withContext(coroutineContext) {
                albums = albumRepository.getAlbumsByArtistId(artistId) as ArrayList<Album>
                withContext(Dispatchers.Main) {
                    LibraryUtil.selectedArtistAlbumList = albums
                    fragment.selectArtist()
                }
            }
        }
    }

    fun getArtistName(): String {
        return LibraryUtil.artists[LibraryUtil.selectedArtist].name
    }
}