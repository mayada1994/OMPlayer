package com.example.android.musicplayerdemo.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.android.musicplayerdemo.livedata.ForeverObserver

fun <T> LiveData<T>.foreverObserver(observer: Observer<T>): ForeverObserver<T> = ForeverObserver(this, observer)