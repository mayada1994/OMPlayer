package com.omplayer.app.viewmodels

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.omplayer.app.adapters.SingleArtistAdapter
import com.omplayer.app.utils.LibraryUtil

class SingleArtistViewModel(application: Application) : AndroidViewModel(application), SingleArtistAdapter.Callback {


    private val _viewLiveData: MutableLiveData<View> = MutableLiveData()
    val viewLiveData: LiveData<View> = _viewLiveData

    val itemAdapter = SingleArtistAdapter(LibraryUtil.selectedArtistAlbumList, this)

    override fun openAlbum(albumId: Int, view: View) {
        _viewLiveData.value = view
    }

    fun getArtistName(): String {
        return LibraryUtil.artists[LibraryUtil.selectedArtist].name
    }


}