package com.omplayer.app.viewmodels

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.omplayer.app.adapters.TrackAdapter
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.repositories.ArtistRepository
import com.omplayer.app.stateMachine.Action
import com.omplayer.app.utils.LibraryUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SingleAlbumViewModel(application: Application) : BaseViewModel(application), TrackAdapter.Callback {

    val itemAdapter = TrackAdapter(callback = this)

    init {
        launch {
            val items = LibraryUtil.selectedAlbumTracklist.map {
                TrackAdapter.Item(it, SingletonHolder.db.artistDao().getArtistById(it.artistId))
            }
            withContext(Dispatchers.Main) {
                itemAdapter.items = items
            }
        }
    }

    private val db = SingletonHolder.db
    private val artistRepository: ArtistRepository = ArtistRepository(db.artistDao())

    private val _viewLiveData: MutableLiveData<View> = MutableLiveData()
    val viewLiveData: LiveData<View> = _viewLiveData

    private val _albumArtistLiveData: MutableLiveData<String> = MutableLiveData()
    val albumLiveData: LiveData<String> = _albumArtistLiveData

    fun getAlbumName(): String {
        return LibraryUtil.currentAlbumList[LibraryUtil.selectedAlbum].title
    }

    fun getAlbumArtist() {
        launch {
            val albumArtist =
                artistRepository.getArtistById(LibraryUtil.currentAlbumList[LibraryUtil.selectedAlbum].artistId)!!.name
            withContext(Dispatchers.Main) {
                _albumArtistLiveData.value = albumArtist
            }
        }
    }

    fun getAlbumYear(): String {
        return LibraryUtil.currentAlbumList[LibraryUtil.selectedAlbum].year
    }

    fun getAlbumCoverUrl(): String {
        return LibraryUtil.currentAlbumList[LibraryUtil.selectedAlbum].cover
    }

    override fun openPlayer(position: Int, view: View) {
        LibraryUtil.tracklist = LibraryUtil.selectedAlbumTracklist
        LibraryUtil.selectedTrack = position
        LibraryUtil.action = Action.Play()
        _viewLiveData.value = view
    }

}