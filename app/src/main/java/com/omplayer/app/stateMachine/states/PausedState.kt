package com.omplayer.app.stateMachine.states

import android.support.v4.media.session.PlaybackStateCompat
import com.omplayer.app.entities.TrackMetadata
import com.omplayer.app.stateMachine.Action
import com.omplayer.app.stateMachine.PlayerContext

class PausedState(context: PlayerContext, private var currentSong: Int) : State(context) {

    override fun handleAction(action: Action): State {
        return when (action) {
            is Action.Play -> {
                context.mediaPlayer.start()

                val playbackStateBuilder = PlaybackStateCompat.Builder()
                playbackStateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY)
                playbackStateBuilder.setState(
                    PlaybackStateCompat.STATE_PLAYING,
                    PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 0.toFloat())

                context.mediaSessionCompat.setPlaybackState(playbackStateBuilder.build())

                PlayingState(context,currentSong)
            }
            is Action.Pause -> this
            is Action.Stop -> {
                context.mediaPlayer.reset()

                val playbackStateBuilder = PlaybackStateCompat.Builder()
                playbackStateBuilder.setActions(PlaybackStateCompat.ACTION_STOP)
                playbackStateBuilder.setState(
                    PlaybackStateCompat.STATE_STOPPED,
                    PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 0.toFloat())

                context.mediaSessionCompat.setPlaybackState(playbackStateBuilder.build())

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
                    context.mediaPlayer.setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.declaredLength)
                    context.mediaPlayer.prepare()
                } catch (e: Exception) {
                }
                context.updateMetadata(TrackMetadata(context.mediaPlayer.duration))
                assetFileDescriptor.close()

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
                    context.mediaPlayer.setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.declaredLength)
                    context.mediaPlayer.prepare()
                } catch (e: Exception) {
                }
                context.updateMetadata(TrackMetadata(context.mediaPlayer.duration))
                assetFileDescriptor.close()

                PausedState(context,currentSong)
            }
        }



    }
}