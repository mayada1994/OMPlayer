package com.example.android.musicplayerdemo.stateMachine.states

import android.support.v4.media.session.PlaybackStateCompat
import com.example.android.musicplayerdemo.entities.TrackMetadata
import com.example.android.musicplayerdemo.stateMachine.Action
import com.example.android.musicplayerdemo.stateMachine.Action.*
import com.example.android.musicplayerdemo.stateMachine.PlayerContext

class PlayingState(context: PlayerContext, private var currentSong: Int) : State(context) {

    override fun handleAction(action: Action): State = when (action) {
        is Play -> this
        is Pause -> {
            context.mediaPlayer.pause()

            PausedState(context, currentSong)
        }
        is Stop -> {
            context.mediaPlayer.reset()

            IdleState(context)
        }
        is Next -> {
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
            context.mediaPlayer.start()
            assetFileDescriptor.close()

            PlayingState(context,currentSong)
        }
        is Prev -> {
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
            context.mediaPlayer.start()
            assetFileDescriptor.close()

            PlayingState(context,currentSong)
        }
    }


}