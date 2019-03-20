package com.example.android.musicplayerdemo.stateMachine

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.session.PlaybackState.*
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.media.session.MediaButtonReceiver
import com.example.android.musicplayerdemo.di.SingletonHolder
import com.example.android.musicplayerdemo.entities.TrackMetadata
import com.example.android.musicplayerdemo.stateMachine.states.IdleState
import com.example.android.musicplayerdemo.stateMachine.states.State

class PlayerManager(override val context: Context) : PlayerContext {

    private val TAG = "PlayerManager"
    override var mediaSessionCompat: MediaSessionCompat = MediaSessionCompat(context,TAG)

    override var mediaPlayer: MediaPlayer = MediaPlayer()
    override val playlist: MutableList<Int> = ArrayList()

    //region LiveData

    private val _currState = MutableLiveData<State>().apply {
        value = IdleState(this@PlayerManager)
    }
    val currState: LiveData<State> = _currState

    private val _metadata = MutableLiveData<TrackMetadata>()
    val metadata: LiveData<TrackMetadata> = _metadata
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
            mediaSessionCompat.isActive = true
            performAction(Action.Play())
            setMediaPlaybackState(PlaybackStateCompat.STATE_PLAYING)
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

        override fun onSeekTo(position: Long) {
            mediaPlayer.seekTo(position.toInt())
        }
    }

    fun initMediaSession() {
        val mediaButtonReceiver = ComponentName(context, MediaButtonReceiver::class.java)
        mediaSessionCompat = MediaSessionCompat(context, TAG, mediaButtonReceiver, null)

        mediaSessionCompat.setCallback(mediaSessionCallback)
        mediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)

        val mediaButtonIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
        mediaButtonIntent.setClass(context, MediaButtonReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, mediaButtonIntent, 0)
        mediaSessionCompat.setMediaButtonReceiver(pendingIntent)
    }

    private fun setMediaPlaybackState(state: Int) {
        val playbackStateBuilder = PlaybackStateCompat.Builder()
        when (state) {
            PlaybackStateCompat.STATE_PLAYING -> {
                playbackStateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE
                        or PlaybackStateCompat.ACTION_PAUSE
                        or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                        or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
            }
            PlaybackStateCompat.STATE_PAUSED -> {
                playbackStateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE
                        or PlaybackStateCompat.ACTION_PLAY
                        or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                        or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
            }
        }

        playbackStateBuilder.setState(state, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 0f)
        mediaSessionCompat.setPlaybackState(playbackStateBuilder.build())
    }

    //endregion

    @MainThread
    fun performAction(action: Action) {
        _currState.value = currState.value!!.handleAction(action)
    }

    @MainThread
    fun setPlaylist(playlist: MutableList<Int>, action: Action? = Action.Play()) {
        mediaPlayer.setOnCompletionListener {
            mediaSessionCallback.onSkipToNext()
        }

        mediaSessionCallback.onStop()
        this.playlist.clear()
        this.playlist.addAll(playlist)

        action?.let {
            performAction(action)
        }
    }


    fun release() {
        mediaPlayer.release()
        mediaSessionCompat.release()
    }

    @MainThread
    override fun updateMetadata(metadata: TrackMetadata) {
        _metadata.value = metadata
    }
}