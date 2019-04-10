package com.omplayer.app.viewmodels

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.omplayer.app.adapters.SingleArtistAdapter
import com.omplayer.app.db.entities.Track
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.repositories.ArtistRepository
import com.omplayer.app.repositories.TrackRepository
import com.omplayer.app.utils.LibraryUtil
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SingleArtistViewModel(application: Application) : AndroidViewModel(application), SingleArtistAdapter.Callback {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.IO
    private val scope = CoroutineScope(coroutineContext)
    private val db = SingletonHolder.db

    private val trackRepository: TrackRepository = TrackRepository(db.trackDao())
    private var tracks = ArrayList<Track>()


    private val _viewLiveData: MutableLiveData<View> = MutableLiveData()
    val viewLiveData: LiveData<View> = _viewLiveData

    val itemAdapter = SingleArtistAdapter(LibraryUtil.selectedArtistAlbumList, this)

    override fun openAlbum(albumId: Int, view: View) {
        scope.launch {
            withContext(coroutineContext) {
                tracks = trackRepository.getTracksByAlbumId(albumId) as ArrayList<Track>
                withContext(Dispatchers.Main) {
                    LibraryUtil.selectedAlbumTracklist = tracks
                    _viewLiveData.value = view
                }
            }
        }
    }

}