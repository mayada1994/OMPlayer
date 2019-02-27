package com.example.android.musicplayerdemo.services.playerService

import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.android.musicplayerdemo.consts.Extra
import com.example.android.musicplayerdemo.di.SingletonHolder
import com.example.android.musicplayerdemo.stateMachine.PlayerManager
import com.example.android.musicplayerdemo.stateMachine.states.IdleState
import com.example.android.musicplayerdemo.stateMachine.states.PausedState
import com.example.android.musicplayerdemo.stateMachine.states.PlayingState

class PlayerServiceManager(val context: Context) {

    private val playerManager: PlayerManager = SingletonHolder.playerManager

    init {
        playerManager.currState.observeForever {
            when (it) {
                is PlayingState -> {
                    startService(Extra.PLAY)
                }
                is PausedState -> {
                    startService(Extra.PAUSE)
                }
                is IdleState -> {
                    stopService()
                }
            }
        }
    }

    private fun startService(action: String) {
        val intent = Intent(context, PlayerService::class.java)
        intent.putExtra(Extra.ACTION, action)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }

    }

    private fun stopService() {
        val intent = Intent(context, PlayerService::class.java)
        context.stopService(intent)
    }


}