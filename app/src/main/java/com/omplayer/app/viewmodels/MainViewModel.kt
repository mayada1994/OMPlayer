package com.omplayer.app.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.omplayer.app.db.entities.Track
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.extensions.foreverObserver
import com.omplayer.app.livedata.ForeverObserver
import com.omplayer.app.utils.LibraryUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {

    private val foreverObservers = mutableListOf<ForeverObserver<*>>()

    init {
        foreverObservers.add(
            LibraryUtil.MainScreenLiveData.foreverObserver(Observer {
                loadTrackData(it)
            })
        )
    }

    private val _currentTrackName = MutableLiveData<String>()
    val currentTrackName: LiveData<String> = _currentTrackName

    private val _currentTrackArtist = MutableLiveData<String>()
    val currentTrackArtist: LiveData<String> = _currentTrackArtist

    private val _currentTrackCover = MutableLiveData<String>()
    val currentTrackCover: LiveData<String> = _currentTrackCover


    fun loadTrackData(currentTrack : Track) {
        CoroutineScope(Dispatchers.IO).launch {
            val currentAlbum = SingletonHolder.db.albumDao().getAlbumById(currentTrack.albumId)
            val currentArtist = SingletonHolder.db.artistDao().getArtistById(currentTrack.artistId)
            withContext(Dispatchers.Main) {
                _currentTrackName.value = currentTrack.title
                _currentTrackArtist.value = currentArtist.name
                _currentTrackCover.value = currentAlbum.cover
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        foreverObservers.forEach { it.release() }
    }



}

