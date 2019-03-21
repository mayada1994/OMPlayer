package com.omplayer.app.stateMachine

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.omplayer.app.db.entities.Track
import com.omplayer.app.stateMachine.states.IdleState
import com.omplayer.app.stateMachine.states.State

class PlayerManager(override val context: Context) : PlayerContext {

    override var mediaPlayer: MediaPlayer = MediaPlayer()
    override val playlist: MutableList<Track> = ArrayList()

    //region LiveData

    private val _currState = MutableLiveData<State>().apply {
        value = IdleState(this@PlayerManager)
    }
    val currState: LiveData<State> = _currState

    private val _metadata = MutableLiveData<Track>()
    val metadata: LiveData<Track> = _metadata
    //endregion

    @MainThread
    fun performAction(action: Action) {
        _currState.value = currState.value!!.handleAction(action)
    }

    @MainThread
    fun setPlaylist(playlist: MutableList<Track>, action: Action? = Action.Play()) {
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
    override fun updateMetadata(metadata: Track) {
        _metadata.value = metadata
    }
}