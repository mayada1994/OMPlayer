package com.example.android.omplayer.stateMachine.states

import com.example.android.omplayer.stateMachine.Action
import com.example.android.omplayer.stateMachine.PlayerContext
import com.example.android.omplayer.utils.LibraryUtil

class PausedState(context: PlayerContext) : State(context) {

    override fun handleAction(action: Action): State {
        return when (action) {
            is Action.Play -> {
                context.mediaPlayer?.start()
                PlayingState(context)
            }
            is Action.Pause -> this
            is Action.Stop -> {
                context.mediaPlayer?.reset()
                IdleState(context)
            }
            is Action.Next -> {
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
                PausedState(context)
            }
            is Action.Prev -> {
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
                PausedState(context)
            }
        }



    }
}