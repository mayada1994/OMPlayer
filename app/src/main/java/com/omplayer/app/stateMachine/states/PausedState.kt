package com.omplayer.app.stateMachine.states

import android.support.v4.media.session.PlaybackStateCompat
import com.omplayer.app.stateMachine.Action
import com.omplayer.app.stateMachine.PlayerContext
import com.omplayer.app.utils.LibraryUtil

class PausedState(context: PlayerContext) : State(context) {

    override fun handleAction(action: Action): State {
        return when (action) {
            is Action.Play -> {
                context.mediaPlayer?.start()

                val playbackStateBuilder = PlaybackStateCompat.Builder()
                playbackStateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY)
                playbackStateBuilder.setState(
                    PlaybackStateCompat.STATE_PLAYING,
                    PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 0.toFloat())

                context.mediaSessionCompat.setPlaybackState(playbackStateBuilder.build())

                PlayingState(context)
            }
            is Action.Pause -> this
            is Action.Stop -> {
                context.mediaPlayer?.reset()

                val playbackStateBuilder = PlaybackStateCompat.Builder()
                playbackStateBuilder.setActions(PlaybackStateCompat.ACTION_STOP)
                playbackStateBuilder.setState(
                    PlaybackStateCompat.STATE_STOPPED,
                    PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 0.toFloat())

                context.mediaSessionCompat.setPlaybackState(playbackStateBuilder.build())

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