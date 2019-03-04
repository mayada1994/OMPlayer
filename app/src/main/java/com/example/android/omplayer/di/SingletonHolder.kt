package com.example.android.omplayer.di

import android.app.Application
import android.content.Context
import com.example.android.omplayer.services.playerService.PlayerServiceManager
import com.example.android.omplayer.stateMachine.PlayerManager


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
        this.application = application
        val playerServiceManager = PlayerServiceManager(context)
    }
}