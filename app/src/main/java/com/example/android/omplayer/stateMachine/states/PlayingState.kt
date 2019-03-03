package com.example.android.omplayer.stateMachine.states

import com.example.android.omplayer.entities.TrackMetadata
import com.example.android.omplayer.stateMachine.Action
import com.example.android.omplayer.stateMachine.Action.*
import com.example.android.omplayer.stateMachine.PlayerContext

class PlayingState(context: PlayerContext, private var currentSong: Int) : State(context) {

    override fun handleAction(action: Action): State = when (action) {
        is Play -> this
        is Pause -> {
            context.mediaPlayer?.pause()
            PausedState(context, currentSong)
        }
        is Stop -> {
            context.mediaPlayer?.reset()
            IdleState(context)
        }
        is Next -> {
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
            context.mediaPlayer?.start()
            PlayingState(context,currentSong)
        }
        is Prev -> {
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
            context.mediaPlayer?.start()
            PlayingState(context,currentSong)
        }
    }


}