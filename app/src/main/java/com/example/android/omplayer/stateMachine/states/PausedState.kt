package com.example.android.omplayer.stateMachine.states

import com.example.android.omplayer.entities.TrackMetadata
import com.example.android.omplayer.stateMachine.Action
import com.example.android.omplayer.stateMachine.PlayerContext

class PausedState(context: PlayerContext, private var currentSong: Int) : State(context) {

    override fun handleAction(action: Action): State {
        return when (action) {
            is Action.Play -> {
                context.mediaPlayer?.start()
                PlayingState(context,currentSong)
            }
            is Action.Pause -> this
            is Action.Stop -> {
                context.mediaPlayer?.reset()
                IdleState(context)
            }
            is Action.Next -> {
                context.mediaPlayer?.reset()
                if (context.playlist.size -1 > currentSong) {
                    currentSong += 1
                }else {
                    currentSong = 0
                }

                try {
                    context.mediaPlayer?.setDataSource(context.playlist[currentSong])
                    context.mediaPlayer?.prepare()
                } catch (e: Exception) {
                }
                context.updateMetadata(TrackMetadata(context.mediaPlayer!!.duration))
                PausedState(context,currentSong)
            }
            is Action.Prev -> {
                context.mediaPlayer?.reset()
                if (currentSong > 0) {
                    currentSong -= 1
                }else {
                    currentSong = 0
                }

                try {
                    context.mediaPlayer?.setDataSource(context.playlist[currentSong])
                    context.mediaPlayer?.prepare()
                } catch (e: Exception) {
                }
                context.updateMetadata(TrackMetadata(context.mediaPlayer!!.duration))
                PausedState(context,currentSong)
            }
        }



    }
}