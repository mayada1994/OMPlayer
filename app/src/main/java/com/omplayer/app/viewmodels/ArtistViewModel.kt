package com.omplayer.app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.omplayer.app.db.entities.Album
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.fragments.ArtistFragment
import com.omplayer.app.repositories.AlbumRepository
import com.omplayer.app.utils.LibraryUtil
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