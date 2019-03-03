package com.example.android.omplayer.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.android.omplayer.livedata.ForeverObserver

fun <T> LiveData<T>.foreverObserver(observer: Observer<T>): ForeverObserver<T> = ForeverObserver(this, observer)