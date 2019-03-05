package com.example.android.musicplayerdemo.di

import android.app.Application
import android.content.Context
import com.example.android.musicplayerdemo.services.playerService.PlayerServiceManager
import com.example.android.musicplayerdemo.stateMachine.Action
import com.example.android.musicplayerdemo.stateMachine.PlayerManager
import com.example.android.musicplayerdemo.viewmodels.PlayerViewModel

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
            PlayerViewModel.MEDIA_RES_2
        )
        this.application = application
        val playerServiceManager = PlayerServiceManager(context)
        playerManager.setPlaylist(playlist, Action.Pause())
    }
}