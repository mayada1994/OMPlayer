package com.example.android.omplayer.stateMachine.states

import com.example.android.omplayer.entities.TrackMetadata
import com.example.android.omplayer.stateMachine.Action
import com.example.android.omplayer.stateMachine.PlayerContext

class IdleState(context: PlayerContext) : State(context) {

    override fun handleAction(action: Action): State = when (action) {
        is Action.Play -> {

            try {
                context.mediaPlayer?.setDataSource(context.playlist[0])
                context.mediaPlayer?.prepare()
            } catch (e: Exception) {
            }
            context.updateMetadata(TrackMetadata(context.mediaPlayer!!.duration))
            context.mediaPlayer?.start()
            PlayingState(context, 0)
        }
        else -> this
    }
}