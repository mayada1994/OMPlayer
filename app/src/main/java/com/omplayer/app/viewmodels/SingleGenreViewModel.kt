package com.omplayer.app.viewmodels

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.omplayer.app.adapters.TrackAdapter
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.stateMachine.Action
import com.omplayer.app.utils.LibraryUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SingleGenreViewModel(application: Application) : BaseViewModel(application), TrackAdapter.Callback {

    override fun openPlayer(position: Int, view: View) {
        LibraryUtil.tracklist = LibraryUtil.selectedGenreTracklist
        LibraryUtil.selectedTrack = position
        LibraryUtil.action = Action.Play()
        _viewLiveData.value = view
    }

    val itemAdapter = TrackAdapter(callback =  this)

    init {
        launch {
            val items = LibraryUtil.selectedGenreTracklist.map {
                TrackAdapter.Item(it, SingletonHolder.db.artistDao().getArtistById(it.artistId))
            }
            withContext(Dispatchers.Main) {
                itemAdapter.items = items
            }
        }
    }


    private val _viewLiveData: MutableLiveData<View> = MutableLiveData()
    val viewLiveData: LiveData<View> = _viewLiveData

    fun getGenreName():String{
        return LibraryUtil.genres[LibraryUtil.selectedGenre].name
    }


}