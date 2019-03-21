package com.omplayer.app.services.playerService

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.omplayer.app.consts.Extra
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.stateMachine.Action


class PlayerBroadcastReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.getSerializableExtra(Extra.ACTION) as Action
        SingletonHolder.playerManager.performAction(action)
        SingletonHolder.playerServiceManager.startService(action)
    }

}