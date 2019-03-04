package com.example.android.omplayer.stateMachine

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.omplayer.entities.TrackMetadata
import com.example.android.omplayer.stateMachine.states.IdleState
import com.example.android.omplayer.stateMachine.states.State

class PlayerManager(override val context: Context) : PlayerContext {

    override var mediaPlayer: MediaPlayer = MediaPlayer()
    override val playlist: MutableList<String> = ArrayList()

    //region LiveData

    private val _currState = MutableLiveData<State>().apply {
        value = IdleState(this@PlayerManager)
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
    fun setPlaylist(playlist: MutableList<String>, action: Action? = Action.Play()) {
        mediaPlayer.setOnCompletionListener {
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
        mediaPlayer.seekTo(position)
    }

    fun release() {
        mediaPlayer.release()
    }

    @MainThread
    override fun updateMetadata(metadata: TrackMetadata) {
        _metadata.value = metadata
    }
}