package com.omplayer.app.viewmodels

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.omplayer.app.adapters.ArtistAdapter
import com.omplayer.app.db.entities.Album
import com.omplayer.app.db.entities.Track
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.fragments.ArtistFragment
import com.omplayer.app.repositories.AlbumRepository
import com.omplayer.app.repositories.TrackRepository
import com.omplayer.app.utils.LibraryUtil
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ArtistViewModel(application: Application) : AndroidViewModel(application), ArtistAdapter.Callback {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.IO
    private val scope = CoroutineScope(coroutineContext)
    private val db = SingletonHolder.db

    private val albumRepository: AlbumRepository = AlbumRepository(db.albumDao())
    private val trackRepository: TrackRepository = TrackRepository(db.trackDao())
    private var albums = ArrayList<Album>()
    private var tracks = ArrayList<Track>()

    private var _viewLiveData: MutableLiveData<View> = MutableLiveData()
    var viewLiveData = _viewLiveData

    val itemAdapter = ArtistAdapter(LibraryUtil.artists, this)


    override fun loadArtistAlbums(artistId: Int, view: View) {
        scope.launch {
            withContext(coroutineContext) {
                tracks = trackRepository.getTracksByArtistId(artistId) as ArrayList<Track>
                tracks.forEach { track ->
                    val currentAlbum: Album = albumRepository.getAlbumById(track.albumId)!!
                    if (!albums.contains(currentAlbum)) {
                        albums.add(currentAlbum)
                    }
                }
                albums.sortBy { it.year }
            }
            withContext(Dispatchers.Main) {
                LibraryUtil.selectedArtistAlbumList = albums
                _viewLiveData.value = view
            }
        }
    }

    fun getArtistName(): String {
        return LibraryUtil.artists[LibraryUtil.selectedArtist].name
    }

    //    fun loadArtistAlbums(artistId: Int, fragment: ArtistFragment) {
//        scope.launch {
//            withContext(coroutineContext) {
//                tracks = trackRepository.getTracksByArtistId(artistId) as ArrayList<Track>
//                tracks.forEach { track ->
//                    val currentAlbum: Album = albumRepository.getAlbumById(track.albumId)!!
//                    if (!albums.contains(currentAlbum)) {
//                        albums.add(currentAlbum)
//                    }
//                }
//                albums.sortBy { it.year }
//            }
//            withContext(Dispatchers.Main) {
//                LibraryUtil.selectedArtistAlbumList = albums
//                fragment.selectArtist()
//            }
//        }
//    }
}