package com.omplayer.app.di

import android.app.Application
import android.content.Context
import com.omplayer.app.db.PlayerDatabase
import com.omplayer.app.services.playerService.PlayerServiceManager
import com.omplayer.app.stateMachine.Action
import com.omplayer.app.stateMachine.PlayerManager
import com.omplayer.app.utils.LibraryUtil


object SingletonHolder {

    lateinit var application: Application
    private val context: Context get() = application
    lateinit var db: PlayerDatabase

    val playerManager by lazy {
        PlayerManager(context)
    }
    val playerServiceManager by lazy {
        PlayerServiceManager(context)
    }

    fun init(application: Application) {
        this.application = application
        playerServiceManager
        db = PlayerDatabase.getDatabase(context)
        playerManager.initMediaSession()

    }
}