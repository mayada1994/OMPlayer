package com.example.android.musicplayerdemo.viewmodels

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.*
import com.example.android.musicplayerdemo.R
import com.example.android.musicplayerdemo.di.SingletonHolder
import com.example.android.musicplayerdemo.entities.TrackMetadata
import com.example.android.musicplayerdemo.extensions.foreverObserver
import com.example.android.musicplayerdemo.livedata.ForeverObserver
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
        const val MEDIA_RES_1 = R.raw.funky_town
        const val MEDIA_RES_2 = R.raw.the_man_who
    }

    private val playlist: MutableList<Int> = mutableListOf(
        MEDIA_RES_1,
        MEDIA_RES_2
    )
    private val playerManager: PlayerManager = SingletonHolder.playerManager

    private val foreverObservers = mutableListOf<ForeverObserver<*>>()

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
        playerManager.setPlaylist(playlist, Action.Pause())
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

    fun onPlayClicked() = playerManager.performAction(Action.Play())

    fun onPauseClicked() = playerManager.performAction(Action.Pause())

    fun onNextClicked() = playerManager.performAction(Action.Next())

    fun onPrevClicked() = playerManager.performAction(Action.Prev())

    fun onStopClicked() = playerManager.performAction(Action.Stop())

    fun onSeek(position: Int) = playerManager.seekTo(position)
    //endregion

}