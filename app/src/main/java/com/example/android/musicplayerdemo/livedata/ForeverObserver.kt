package com.example.android.musicplayerdemo.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class ForeverObserver<T>(private val liveData: LiveData<T>, private val observer: Observer<T>) {

    init {
        liveData.observeForever(observer)
    }

    fun release() {
        liveData.removeObserver(observer)
    }
}