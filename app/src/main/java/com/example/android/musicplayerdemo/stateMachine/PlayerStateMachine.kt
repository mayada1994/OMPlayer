package com.example.android.musicplayerdemo.stateMachine

import android.content.Context
import android.media.MediaPlayer
import com.example.android.musicplayerdemo.stateMachine.states.IdleState
import com.example.android.musicplayerdemo.stateMachine.states.State
import java.util.concurrent.ScheduledExecutorService

class PlayerStateMachine(override val context: Context, override val callback: SeekbarCallback) : PlayerContext {

    companion object {
        const val PLAYBACK_POSITION_REFRESH_INTERVAL_MS = 1000
    }

    override var mediaPlayer: MediaPlayer? = null
    override val playlist: MutableList<Int> = ArrayList()
    override var executor: ScheduledExecutorService? = null
    override var seekbarPositionUpdateTask: Runnable? = null

    var currState: State = IdleState(this)

    fun performAction(action: Action) {
        currState = currState.handleAction(action)
    }

    fun setPlaylist(playlist: MutableList<Int>, action: Action? = Action.Play()) {
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setOnCompletionListener {
            currState.handleAction(Action.Next())
        }

        currState.handleAction(Action.Stop())
        this.playlist.clear()
        this.playlist.addAll(playlist)
        action?.let {
            currState.handleAction(action)
        }
    }

    fun seekTo(position: Int) {
        if (mediaPlayer != null) {
            mediaPlayer!!.seekTo(position)
        }
    }

    fun release() {
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }
}