package com.omplayer.app.viewmodels

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.omplayer.app.adapters.AlbumAdapter
import com.omplayer.app.db.entities.Track
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.repositories.TrackRepository
import com.omplayer.app.utils.LibraryUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumViewModel(application: Application) : BaseViewModel(application), AlbumAdapter.Callback {

    private val db = SingletonHolder.db

    private val trackRepository: TrackRepository = TrackRepository(db.trackDao())
    private var tracks = ArrayList<Track>()

    val itemAdapter = AlbumAdapter(LibraryUtil.currentAlbums, this)

    private var _viewLiveData: MutableLiveData<View> = MutableLiveData()
    var viewLiveData = _viewLiveData


    override fun loadAlbumTracks(albumId: Int, view: View) {
        launch {
            tracks = trackRepository.getTracksByAlbumId(albumId) as ArrayList<Track>
            withContext(Dispatchers.Main) {
                LibraryUtil.selectedAlbumTracklist = tracks
                _viewLiveData.value = view
            }
        }
    }

    fun filterAlbumsByYear(startYear: String, endYear: String) {
        launch {
            LibraryUtil.filteredAlbumList = db.albumDao().getAlbumsByYears(startYear, endYear)
            withContext(Dispatchers.Main) {
                itemAdapter.albums = LibraryUtil.filteredAlbumList
                LibraryUtil.currentAlbums = LibraryUtil.filteredAlbumList
                itemAdapter.notifyDataSetChanged()
            }
        }
    }

    fun restoreAlbums() {
        itemAdapter.albums = LibraryUtil.albums
        LibraryUtil.currentAlbums = LibraryUtil.albums
        itemAdapter.notifyDataSetChanged()
    }

}