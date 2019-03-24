package com.omplayer.app.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.omplayer.app.livedata.ForeverObserver

fun <T> LiveData<T>.foreverObserver(observer: Observer<T>): ForeverObserver<T> = ForeverObserver(this, observer)