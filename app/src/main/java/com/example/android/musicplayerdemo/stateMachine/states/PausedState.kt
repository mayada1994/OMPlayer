package com.example.android.musicplayerdemo.stateMachine.states

import com.example.android.musicplayerdemo.entities.TrackMetadata
import com.example.android.musicplayerdemo.stateMachine.Action
import com.example.android.musicplayerdemo.stateMachine.PlayerContext

class PausedState(context: PlayerContext, private var currentSong: Int) : State(context) {

    override fun handleAction(action: Action): State {
        return when (action) {
            is Action.Play -> {
                context.mediaPlayer.start()
                PlayingState(context,currentSong)
            }
            is Action.Pause -> this
            is Action.Stop -> {
                context.mediaPlayer.reset()
                IdleState(context)
            }
            is Action.Next -> {
                context.mediaPlayer.reset()
                if (context.playlist.size -1 > currentSong) {
                    currentSong += 1
                }else {
                    currentSong = 0
                }
                val assetFileDescriptor = context.context.resources.openRawResourceFd(context.playlist[currentSong])
                try {
                    context.mediaPlayer.setDataSource(assetFileDescriptor)
                    context.mediaPlayer.prepare()
                } catch (e: Exception) {
                }
                context.updateMetadata(TrackMetadata(context.mediaPlayer.duration))
                PausedState(context,currentSong)
            }
            is Action.Prev -> {
                context.mediaPlayer.reset()
                if (currentSong > 0) {
                    currentSong -= 1
                }else {
                    currentSong = 0
                }
                val assetFileDescriptor = context.context.resources.openRawResourceFd(context.playlist[currentSong])
                try {
                    context.mediaPlayer.setDataSource(assetFileDescriptor)
                    context.mediaPlayer.prepare()
                } catch (e: Exception) {
                }
                context.updateMetadata(TrackMetadata(context.mediaPlayer.duration))
                PausedState(context,currentSong)
            }
        }



    }
}