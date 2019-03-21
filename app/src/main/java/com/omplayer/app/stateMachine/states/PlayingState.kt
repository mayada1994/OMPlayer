package com.omplayer.app.stateMachine.states

import com.omplayer.app.stateMachine.Action
import com.omplayer.app.stateMachine.Action.*
import com.omplayer.app.stateMachine.PlayerContext
import com.omplayer.app.utils.LibraryUtil

class PlayingState(context: PlayerContext) : State(context) {

    override fun handleAction(action: Action): State = when (action) {
        is Play -> this
        is Pause -> {
            context.mediaPlayer?.pause()
            PausedState(context)
        }
        is Stop -> {
            context.mediaPlayer?.reset()
            IdleState(context)
        }
        is Next -> {
            context.mediaPlayer?.reset()
            if (context.playlist.size -1 > LibraryUtil.selectedTrack) {
                LibraryUtil.selectedTrack += 1
            }else {
                LibraryUtil.selectedTrack = 0
            }

            try {
                context.mediaPlayer?.setDataSource(context.playlist[LibraryUtil.selectedTrack].path)
                context.mediaPlayer?.prepare()
            } catch (e: Exception) {
            }
            context.updateMetadata(context.playlist[LibraryUtil.selectedTrack])
            context.mediaPlayer?.start()
            PlayingState(context)
        }
        is Prev -> {
            context.mediaPlayer?.reset()
            if (LibraryUtil.selectedTrack > 0) {
                LibraryUtil.selectedTrack -= 1
            }else {
                LibraryUtil.selectedTrack = 0
            }

            try {
                context.mediaPlayer?.setDataSource(context.playlist[LibraryUtil.selectedTrack].path)
                context.mediaPlayer?.prepare()
            } catch (e: Exception) {
            }
            context.updateMetadata(context.playlist[LibraryUtil.selectedTrack])
            context.mediaPlayer?.start()
            PlayingState(context)
        }
    }


}