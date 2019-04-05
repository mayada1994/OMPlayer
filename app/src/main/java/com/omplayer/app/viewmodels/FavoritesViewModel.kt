package com.omplayer.app.viewmodels

import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.omplayer.app.adapters.FavoritesAdapter
import com.omplayer.app.di.SingletonHolder.application
import com.omplayer.app.utils.LibraryUtil


class FavoritesViewModel(application: Application) : AndroidViewModel(application), FavoritesAdapter.Callback {

    private val _viewLiveData: MutableLiveData<View> = MutableLiveData()
    val viewLiveData: LiveData<View> = _viewLiveData

    val itemAdapter = FavoritesAdapter(LibraryUtil.favorites, this)

    override fun openPlayer(view: View) {

        _viewLiveData.value = view

    }

}