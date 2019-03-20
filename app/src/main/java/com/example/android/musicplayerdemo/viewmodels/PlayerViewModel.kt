package com.example.android.musicplayerdemo.viewmodels

import android.app.Application
import android.content.ComponentName
import android.os.Handler
import android.os.Looper
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.*
import com.example.android.musicplayerdemo.R
import com.example.android.musicplayerdemo.di.SingletonHolder
import com.example.android.musicplayerdemo.entities.TrackMetadata
import com.example.android.musicplayerdemo.extensions.foreverObserver
import com.example.android.musicplayerdemo.livedata.ForeverObserver
import com.example.android.musicplayerdemo.services.playerService.PlayerService
import com.example.android.musicplayerdemo.stateMachine.Action
import com.example.android.musicplayerdemo.stateMachine.PlayerManager
import com.example.android.musicplayerdemo.stateMachine.states.IdleState
import com.example.android.musicplayerdemo.stateMachine.states.PlayingState
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class PlayerViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {

    companion object {
        val TAG: String = PlayerViewModel::class.java.simpleName

        const val MEDIA_RES_1 = R.raw.funky_town
        const val MEDIA_RES_2 = R.raw.the_man_who
        const val MEDIA_RES_3 = R.raw.country_roads
    }

    private val playerManager: PlayerManager = SingletonHolder.playerManager

    private val foreverObservers = mutableListOf<ForeverObserver<*>>()

    //region MediaLibraryCompat

    private val mediaControllerCompat: MediaControllerCompat by lazy {
        MediaControllerCompat(
            application,
            playerManager.mediaSessionCompat.sessionToken
        ).apply {
            registerCallback(
                object : MediaControllerCompat.Callback() {

                    override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
                        super.onPlaybackStateChanged(state)
                        Log.d(TAG, "onPlaybackStateChanged: $state")
                    }
                }
            )
        }
    }

    //endregion

    //region LiveData

    private val _currentPosition = MutableLiveData<Int?>()
    val currentPosition: LiveData<Int?> = _currentPosition

    private val _metadata = MediatorLiveData<TrackMetadata?>().apply {
        addSource(playerManager.metadata) { value = it }
    }
    val metadata: LiveData<TrackMetadata?> = _metadata
    //endregion

    private val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    private val seekbarPositionUpdateTask: () -> Unit = {
        Handler(Looper.getMainLooper()).post {
            _currentPosition.value = playerManager.mediaPlayer.currentPosition
        }
    }
    private var scheduledTask: ScheduledFuture<*>? = null

    init {
        foreverObservers.add(
            playerManager.currState.foreverObserver(Observer {
                when (it) {
                    is PlayingState -> {
                        startUpdateSeekbar()
                    }
                    is IdleState -> {
                        _currentPosition.value = 0
                    }
                    else -> {
                        stopUpdateSeekbar()
                    }
                }
            })
        )
    }

    override fun onCleared() {
        super.onCleared()
        foreverObservers.forEach { it.release() }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        startUpdateSeekbar()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        stopUpdateSeekbar()
    }

    fun startUpdateSeekbar() {
        if (scheduledTask == null) {
            scheduledTask = executor.scheduleAtFixedRate(
                seekbarPositionUpdateTask,
                0,
                1,
                TimeUnit.SECONDS
            )
        }
    }

    fun stopUpdateSeekbar() {
        scheduledTask?.cancel(true)
        scheduledTask = null
    }

    //region View interaction

    fun onPlayClicked() = mediaControllerCompat.transportControls.play()

    fun onPauseClicked() = mediaControllerCompat.transportControls.pause()

    fun onNextClicked() = mediaControllerCompat.transportControls.skipToNext()

    fun onPrevClicked() = mediaControllerCompat.transportControls.skipToPrevious()

    fun onStopClicked() = mediaControllerCompat.transportControls.stop()

    fun onSeek(position: Int) = mediaControllerCompat.transportControls.seekTo(position.toLong())

    //endregion

}