package com.example.android.musicplayerdemo.services.playerService

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.example.android.musicplayerdemo.consts.Extra
import com.example.android.musicplayerdemo.di.SingletonHolder
import com.example.android.musicplayerdemo.di.SingletonHolder.application
import com.example.android.musicplayerdemo.di.SingletonHolder.playerManager
import com.example.android.musicplayerdemo.stateMachine.Action
import com.example.android.musicplayerdemo.viewmodels.PlayerViewModel

class PlayerBroadcastReceiver : BroadcastReceiver() {

    private val mediaControllerCompat: MediaControllerCompat by lazy {
        MediaControllerCompat(
            application,
            playerManager.mediaSessionCompat.sessionToken
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
        val action = intent.getSerializableExtra(Extra.ACTION) as Action

        when (action) {
            is Action.Play -> mediaControllerCompat.transportControls.play()
            is Action.Pause -> mediaControllerCompat.transportControls.pause()
            is Action.Next -> mediaControllerCompat.transportControls.skipToNext()
            is Action.Prev -> mediaControllerCompat.transportControls.skipToPrevious()
            is Action.Stop -> mediaControllerCompat.transportControls.stop()
        }
        SingletonHolder.playerServiceManager.startService(action)
    }

}