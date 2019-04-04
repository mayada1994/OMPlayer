package com.omplayer.app.viewmodels

import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.omplayer.app.adapters.TrackAdapter
import com.omplayer.app.utils.LibraryUtil

class TrackViewModel(application: Application) : AndroidViewModel(application), TrackAdapter.Callback {

    val itemAdapter = TrackAdapter(LibraryUtil.tracks, this)

    private var _viewLiveData: MutableLiveData<View> = MutableLiveData()
    var viewLiveData = _viewLiveData

    override fun openPlayer(view: View) {
        _viewLiveData.value = view
    }


}