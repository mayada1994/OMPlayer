package com.omplayer.app.di

import android.app.Application
import android.content.Context
import com.omplayer.app.services.playerService.PlayerServiceManager
import com.omplayer.app.stateMachine.Action
import com.omplayer.app.stateMachine.PlayerManager
import com.omplayer.app.viewmodels.PlayerViewModel

object SingletonHolder {

    lateinit var application: Application
    private val context: Context get() = application

    val playerManager by lazy {
        PlayerManager(context)
    }
    val playerServiceManager by lazy {
        PlayerServiceManager(context)
    }

    fun init(application: Application) {

        val playlist: MutableList<Int> = mutableListOf(
            PlayerViewModel.MEDIA_RES_1,
            PlayerViewModel.MEDIA_RES_2,
            PlayerViewModel.MEDIA_RES_3
        )
        this.application = application
        val playerServiceManager = PlayerServiceManager(context)
        playerManager.initMediaSession()
        playerManager.setPlaylist(playlist, Action.Pause())
    }
}