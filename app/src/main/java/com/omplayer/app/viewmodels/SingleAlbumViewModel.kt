package com.omplayer.app.viewmodels

import android.app.Application
import android.view.View
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.omplayer.app.adapters.SingleAlbumAdapter
import com.omplayer.app.adapters.SingleGenreAdapter
import com.omplayer.app.db.entities.Track
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.repositories.ArtistRepository
import com.omplayer.app.repositories.TrackRepository
import com.omplayer.app.utils.LibraryUtil
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SingleAlbumViewModel(application: Application) : AndroidViewModel(application), SingleAlbumAdapter.Callback {

    val itemAdapter = SingleAlbumAdapter(LibraryUtil.selectedAlbumTracklist, this)

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.IO
    private val scope = CoroutineScope(coroutineContext)
    private val db = SingletonHolder.db

    private val artistRepository: ArtistRepository = ArtistRepository(db.artistDao())

    private val _viewLiveData: MutableLiveData<View> = MutableLiveData()
    val viewLiveData: LiveData<View> = _viewLiveData

    fun getAlbumName(): String {
        return LibraryUtil.currentAlbumList[LibraryUtil.selectedAlbum].title
    }

    fun getAlbumArtist(albumArtistName: TextView) {
        scope.launch {
            withContext(coroutineContext) {
                val albumArtist =
                    artistRepository.getArtistById(LibraryUtil.currentAlbumList[LibraryUtil.selectedAlbum].artistId)!!.name
                withContext(Dispatchers.Main) {
                    albumArtistName.text = albumArtist
                }
            }
        }
    }

    fun getAlbumYear(): String {
        return LibraryUtil.currentAlbumList[LibraryUtil.selectedAlbum].year
    }

    fun getAlbumCoverUrl(): String {
        return LibraryUtil.currentAlbumList[LibraryUtil.selectedAlbum].cover
    }

    override fun openPlayer(view: View) {
        _viewLiveData.value = view
    }





}