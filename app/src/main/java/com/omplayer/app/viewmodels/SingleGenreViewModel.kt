package com.omplayer.app.viewmodels

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.omplayer.app.adapters.SingleGenreAdapter
import com.omplayer.app.utils.LibraryUtil

class SingleGenreViewModel(application: Application) : AndroidViewModel(application), SingleGenreAdapter.Callback {

    val itemAdapter = SingleGenreAdapter(LibraryUtil.selectedGenreTracklist, this)


    private val _viewLiveData: MutableLiveData<View> = MutableLiveData()
    val viewLiveData: LiveData<View> = _viewLiveData

    fun getGenreName():String{
        return LibraryUtil.genres[LibraryUtil.selectedGenre].name
    }

    override fun openPlayer(view: View) {
        _viewLiveData.value = view
    }

}