package com.omplayer.app.stateMachine.states

import com.omplayer.app.stateMachine.Action
import com.omplayer.app.stateMachine.PlayerContext
import com.omplayer.app.utils.LibraryUtil

class IdleState(context: PlayerContext) : State(context) {

    override fun handleAction(action: Action): State = when (action) {
        is Action.Play -> {

            try {
                context.mediaPlayer?.setDataSource(context.playlist[LibraryUtil.selectedTrack].path)
                context.mediaPlayer?.prepare()
            } catch (e: Exception) {
            }
            context.updateMetadata(context.playlist[LibraryUtil.selectedTrack])
            context.mediaPlayer?.start()
            PlayingState(context)
        }
        else -> this
    }
}