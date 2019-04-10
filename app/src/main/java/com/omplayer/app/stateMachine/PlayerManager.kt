package com.omplayer.app.stateMachine

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.media.session.MediaButtonReceiver
import com.omplayer.app.db.entities.Track
import com.omplayer.app.stateMachine.states.IdleState
import com.omplayer.app.stateMachine.states.PlayingState
import com.omplayer.app.stateMachine.states.State
import com.omplayer.app.utils.LibraryUtil


@Suppress("DEPRECATION")
class PlayerManager(override val context: Context) : PlayerContext, AudioManager.OnAudioFocusChangeListener {


    private val TAG = "PlayerManager"

    //region Context Implementation

    override var mediaSessionCompat: MediaSessionCompat = MediaSessionCompat(context, TAG)
    override var mediaPlayer: MediaPlayer = MediaPlayer()
    override val playlist: MutableList<Track> = ArrayList()
    override var isShuffle: Boolean = false
    override var isLooping: Boolean = false

    //endregion

    //region LiveData

    private val _currState = MutableLiveData<State>().apply {
        value = IdleState(this@PlayerManager)
    }
    val currState: LiveData<State> = _currState

    private val _metadata = MutableLiveData<Track>()
    val metadata: LiveData<Track> = _metadata
    //endregion

    //region AudioManager

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_LOSS -> {
                if (_currState.value is PlayingState) {
                    performAction(Action.Pause())
                }
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                performAction(Action.Pause())
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                mediaPlayer.setVolume(0.3f, 0.3f)
            }
            AudioManager.AUDIOFOCUS_GAIN -> {
                if (_currState.value is IdleState) {
                    performAction(Action.Pause())
                }
                mediaPlayer.setVolume(1.0f, 1.0f)
            }
        }
    }


    //endregion

    //region mediaSessionCompat

    private val mediaSessionCallback = object : MediaSessionCompat.Callback() {

        override fun onPause() {
            super.onPause()
            performAction(Action.Pause())
            setMediaPlaybackState(PlaybackStateCompat.STATE_PAUSED)
        }

        override fun onPlay() {
            super.onPlay()

            val result = audioManager.requestAudioFocus(
                this@PlayerManager,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN
            )

            if (result == AudioManager.AUDIOFOCUS_GAIN) {
                mediaSessionCompat.isActive = true
                performAction(Action.Play())
                setMediaPlaybackState(PlaybackStateCompat.STATE_PLAYING)
            }
        }

        override fun onSkipToPrevious() {
            super.onSkipToPrevious()
            performAction(Action.Prev())
        }

        override fun onSkipToNext() {
            super.onSkipToNext()
            performAction(Action.Next())
        }

        override fun onStop() {
            super.onStop()
            performAction(Action.Stop())
        }

        override fun onSetRepeatMode(repeatMode: Int) {
            super.onSetRepeatMode(repeatMode)
            when (repeatMode) {
                PlaybackStateCompat.REPEAT_MODE_ALL -> {
                    isLooping = false
                    mediaPlayer.isLooping = isLooping
                }
                PlaybackStateCompat.REPEAT_MODE_GROUP -> {
                    isLooping = false
                    mediaPlayer.isLooping = isLooping
                }
                PlaybackStateCompat.REPEAT_MODE_INVALID -> {
                    isLooping = false
                    mediaPlayer.isLooping = isLooping
                }
                PlaybackStateCompat.REPEAT_MODE_NONE -> {
                    isLooping = false
                    mediaPlayer.isLooping = isLooping
                }
                PlaybackStateCompat.REPEAT_MODE_ONE -> {
                    isLooping = true
                    mediaPlayer.isLooping = isLooping
                }
            }
        }

        override fun onSetShuffleMode(shuffleMode: Int) {
            super.onSetShuffleMode(shuffleMode)
            when (shuffleMode) {
                PlaybackStateCompat.SHUFFLE_MODE_ALL -> {
                    isShuffle = true
                }
                PlaybackStateCompat.SHUFFLE_MODE_NONE -> {
                    isShuffle = false
                }
                PlaybackStateCompat.SHUFFLE_MODE_GROUP -> {
                    isShuffle = false
                }
                PlaybackStateCompat.SHUFFLE_MODE_INVALID -> {
                    isShuffle = false
                }
            }
        }


        override fun onSeekTo(position: Long) {
            mediaPlayer.seekTo(position.toInt())
        }
    }

    fun initMediaSession() {
        val mediaButtonReceiver = ComponentName(context, MediaButtonReceiver::class.java)
        mediaSessionCompat = MediaSessionCompat(context, TAG, mediaButtonReceiver, null)

        mediaSessionCompat.setCallback(mediaSessionCallback)
        mediaSessionCompat.setFlags(
            MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                    or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        )

        val mediaButtonIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
        mediaButtonIntent.setClass(context, MediaButtonReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, mediaButtonIntent, 0)
        mediaSessionCompat.setMediaButtonReceiver(pendingIntent)
    }

    private fun setMediaPlaybackState(state: Int) {
        val playbackStateBuilder = PlaybackStateCompat.Builder()
        when (state) {
            PlaybackStateCompat.STATE_PLAYING -> {
                playbackStateBuilder.setActions(
                    PlaybackStateCompat.ACTION_PLAY_PAUSE
                            or PlaybackStateCompat.ACTION_PAUSE
                            or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                            or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                )
            }
            PlaybackStateCompat.STATE_PAUSED -> {
                playbackStateBuilder.setActions(
                    PlaybackStateCompat.ACTION_PLAY_PAUSE
                            or PlaybackStateCompat.ACTION_PLAY
                            or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                            or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                )
            }
        }

        playbackStateBuilder.setState(state, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 0f)
        mediaSessionCompat.setPlaybackState(playbackStateBuilder.build())
    }

//endregion

    @MainThread
    fun performAction(action: Action) {
        _currState.value = _currState.value!!.handleAction(action)
    }

    @MainThread
    fun setPlaylist(playlist: List<Track>) {
        mediaPlayer.setOnCompletionListener {
            mediaSessionCallback.onSkipToNext()
            LibraryUtil.MainScreenLiveData.value = LibraryUtil.tracklist[LibraryUtil.selectedTrack]
        }

        mediaPlayer.setVolume(1.0f, 1.0f)

        mediaSessionCallback.onStop()
        this.playlist.clear()
        this.playlist.addAll(playlist)
    }


    fun release() {
        mediaPlayer.release()
        mediaSessionCompat.release()
    }

    @MainThread
    override fun updateMetadata(metadata: Track) {
        _metadata.value = metadata
    }

}