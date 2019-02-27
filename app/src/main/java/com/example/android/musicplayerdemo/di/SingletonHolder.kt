package com.example.android.musicplayerdemo.di

import android.app.Application
import android.content.Context
import com.example.android.musicplayerdemo.services.playerService.PlayerServiceManager
import com.example.android.musicplayerdemo.stateMachine.PlayerManager

object SingletonHolder {

    lateinit var application: Application
    private val context: Context get() = application

    val playerManager by lazy {
        PlayerManager(context)
    }

    fun init(application: Application) {
        this.application = application
        val playerServiceManager = PlayerServiceManager(context)
    }
}