package com.example.android.musicplayerdemo.services.playerService

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.example.android.musicplayerdemo.di.SingletonHolder
import com.example.android.musicplayerdemo.viewmodels.PlayerViewModel

class BecomingNoisyReceiver : BroadcastReceiver() {

    private val mediaControllerCompat: MediaControllerCompat by lazy {
        MediaControllerCompat(
            SingletonHolder.application,
            SingletonHolder.playerManager.mediaSessionCompat.sessionToken
        ).apply {
            registerCallback(
                object : MediaControllerCompat.Callback() {

                    override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
                        super.onPlaybackStateChanged(state)
                        Log.d(PlayerViewModel.TAG, "onPlaybackStateChanged: $state")
                    }
                }
            )
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
            mediaControllerCompat.transportControls.pause()
        }
    }


}