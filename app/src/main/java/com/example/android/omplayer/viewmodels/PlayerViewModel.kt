package com.example.android.omplayer.viewmodels

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.*
import com.example.android.omplayer.entities.TrackMetadata
import com.example.android.omplayer.entities.LibraryUtil
import com.example.android.omplayer.extensions.foreverObserver
import com.example.android.omplayer.livedata.ForeverObserver
import com.example.android.omplayer.stateMachine.Action
import com.example.android.omplayer.stateMachine.PlayerStateMachine
import com.example.android.omplayer.stateMachine.states.IdleState
import com.example.android.omplayer.stateMachine.states.PlayingState
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class PlayerViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {

    private val playlist: MutableList<String> = LibraryUtil.tracklist
    private val playerSM: PlayerStateMachine = PlayerStateMachine(application)

    private val foreverObservers = mutableListOf<ForeverObserver<*>>()

    //region LiveData

    private val _currentPosition = MutableLiveData<Int?>()
    val currentPosition: LiveData<Int?> = _currentPosition

    private val _metadata = MediatorLiveData<TrackMetadata?>().apply {
        addSource(playerSM.metadata) { value = it }
    }
    val metadata: LiveData<TrackMetadata?> = _metadata
    //endregion

    private val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    private val seekbarPositionUpdateTask: () -> Unit = {
        Handler(Looper.getMainLooper()).post {
            _currentPosition.value = playerSM.mediaPlayer!!.currentPosition
        }
    }
    private var scheduledTask: ScheduledFuture<*>? = null

    init {
        foreverObservers.add(
            playerSM.currState.foreverObserver(Observer {
                when (it) {
                    is PlayingState -> {
                        if (scheduledTask == null) {
                            scheduledTask = executor.scheduleAtFixedRate(
                                seekbarPositionUpdateTask,
                                0,
                                1,
                                TimeUnit.SECONDS
                            )
                        }
                    }
                    is IdleState -> {
                        _currentPosition.value = 0
                    }
                    else -> {
                        scheduledTask?.cancel(true)
                        scheduledTask = null
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
        playerSM.setPlaylist(playlist, Action.Pause())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        playerSM.release()
    }

    //region View interaction

    fun onPlayClicked() = playerSM.performAction(Action.Play())

    fun onPauseClicked() = playerSM.performAction(Action.Pause())

    fun onNextClicked() = playerSM.performAction(Action.Next())

    fun onPrevClicked() = playerSM.performAction(Action.Prev())

    fun onStopClicked() = playerSM.performAction(Action.Stop())

    fun onSeek(position: Int) = playerSM.seekTo(position)
    //endregion
}