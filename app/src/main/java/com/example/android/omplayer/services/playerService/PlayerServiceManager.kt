package com.example.android.omplayer.services.playerService

import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.android.omplayer.consts.Extra
import com.example.android.omplayer.di.SingletonHolder
import com.example.android.omplayer.stateMachine.Action
import com.example.android.omplayer.stateMachine.PlayerManager
import com.example.android.omplayer.stateMachine.states.IdleState
import com.example.android.omplayer.stateMachine.states.PausedState
import com.example.android.omplayer.stateMachine.states.PlayingState

class PlayerServiceManager(val context: Context) {

    private val playerManager: PlayerManager = SingletonHolder.playerManager

    init {
        playerManager.currState.observeForever {
            when (it) {
                is PlayingState -> {
                    startService(Action.Play())
                }
                is PausedState -> {
                    startService(Action.Pause())
                }
                is IdleState -> {
                    stopService()
                }
            }
        }
    }

    fun startService(action: Action) {
        val intent = Intent(context, PlayerService::class.java)
        intent.putExtra(Extra.ACTION, action)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    fun stopService() {
        val intent = Intent(context, PlayerService::class.java)
        context.stopService(intent)
    }


}