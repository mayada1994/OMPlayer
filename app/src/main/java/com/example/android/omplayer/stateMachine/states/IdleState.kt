package com.example.android.omplayer.stateMachine.states

import com.example.android.omplayer.stateMachine.Action
import com.example.android.omplayer.stateMachine.PlayerContext
import com.example.android.omplayer.utils.LibraryUtil

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