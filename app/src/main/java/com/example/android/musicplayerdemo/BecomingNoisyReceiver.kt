package com.example.android.musicplayerdemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import com.example.android.musicplayerdemo.di.SingletonHolder
import com.example.android.musicplayerdemo.stateMachine.Action

class BecomingNoisyReceiver : BroadcastReceiver() {
// TODO: Make this broadcast receiver works!!!

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
            SingletonHolder.playerManager.performAction(Action.Pause())
        }
    }


}