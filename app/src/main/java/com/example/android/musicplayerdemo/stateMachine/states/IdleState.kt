package com.example.android.musicplayerdemo.stateMachine.states

import com.example.android.musicplayerdemo.stateMachine.Action
import com.example.android.musicplayerdemo.stateMachine.PlayerContext
import com.example.android.musicplayerdemo.stateMachine.PlayerStateMachine.Companion.PLAYBACK_POSITION_REFRESH_INTERVAL_MS
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class IdleState(context: PlayerContext) : State(context) {

    override fun handleAction(action: Action): State = when (action) {
        is Action.Play -> {
            val assetFileDescriptor = context.context.resources.openRawResourceFd(context.playlist[0])
            try {
                context.mediaPlayer?.setDataSource(assetFileDescriptor)
                context.mediaPlayer?.prepare()
            } catch (e: Exception) {
            }
            val duration = context.mediaPlayer!!.duration
            context.callback.updateSeekbarDuration(duration)
            context.mediaPlayer?.start()
            /**
             * Syncs the mediaPlayer position with SeekbarCallback via recurring task.
             */
            if (context.executor == null) {
                context.executor = Executors.newSingleThreadScheduledExecutor()
            }
            if (context.seekbarPositionUpdateTask == null) {
                context.seekbarPositionUpdateTask =
                    Runnable {
                        val currentPosition = context.mediaPlayer!!.currentPosition
                        context.callback.updateSeekbarPosition(currentPosition)
                    }
            }
            context.executor?.scheduleAtFixedRate(
                context.seekbarPositionUpdateTask,
                0,
                PLAYBACK_POSITION_REFRESH_INTERVAL_MS.toLong(),
                TimeUnit.MILLISECONDS
            )

            PlayingState(context, 0)
        }
        else -> this
    }
}