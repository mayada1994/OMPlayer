package com.example.android.musicplayerdemo.stateMachine

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.musicplayerdemo.entities.TrackMetadata
import com.example.android.musicplayerdemo.stateMachine.states.IdleState
import com.example.android.musicplayerdemo.stateMachine.states.State
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class PlayerStateMachine(override val context: Context) : PlayerContext {

    override var mediaPlayer: MediaPlayer? = null//TODO: not null
    override val playlist: MutableList<Int> = ArrayList()

    //region LiveData

    private val _currState = MutableLiveData<State>().apply {
        value = IdleState(this@PlayerStateMachine)
    }
    val currState: LiveData<State> = _currState

    private val _metadata = MutableLiveData<TrackMetadata>()
    val metadata: LiveData<TrackMetadata> = _metadata
    //endregion

    @MainThread
    fun performAction(action: Action) {
        _currState.value = currState.value!!.handleAction(action)
    }

    @MainThread
    fun setPlaylist(playlist: MutableList<Int>, action: Action? = Action.Play()) {
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setOnCompletionListener {
            performAction(Action.Next())
        }

        performAction(Action.Stop())
        this.playlist.clear()
        this.playlist.addAll(playlist)

        action?.let {
            performAction(action)
        }
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    fun release() {
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }

    @MainThread
    override fun updateMetadata(metadata: TrackMetadata) {
        _metadata.value = metadata
    }
}