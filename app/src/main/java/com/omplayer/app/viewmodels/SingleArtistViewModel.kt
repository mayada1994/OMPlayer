package com.omplayer.app.viewmodels

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.omplayer.app.adapters.SingleArtistAdapter
import com.omplayer.app.db.entities.Track
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.repositories.TrackRepository
import com.omplayer.app.utils.LibraryUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SingleArtistViewModel(application: Application) : BaseViewModel(application), SingleArtistAdapter.Callback {

    //    private var parentJob = Job()
//    private val coroutineContext: CoroutineContext
//        get() = parentJob + Dispatchers.IO
//    private val scope = CoroutineScope(coroutineContext)
    private val db = SingletonHolder.db

    private val trackRepository: TrackRepository = TrackRepository(db.trackDao())
    private var tracks = ArrayList<Track>()


    private val _viewLiveData: MutableLiveData<View> = MutableLiveData()
    val viewLiveData: LiveData<View> = _viewLiveData

    val itemAdapter = SingleArtistAdapter(LibraryUtil.selectedArtistAlbumList, this)

    override fun openAlbum(albumId: Int, view: View) {
        launch {
            tracks = trackRepository.getTracksByAlbumId(albumId) as ArrayList<Track>
            withContext(Dispatchers.Main) {
                LibraryUtil.selectedAlbumTracklist = tracks
                _viewLiveData.value = view
            }
        }

    }
}