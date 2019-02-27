package com.example.android.musicplayerdemo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.android.musicplayerdemo.di.SingletonHolder

class App : Application() {
    companion object {
        const val CHANNEL_ID = "playerServiceChannel"
    }

    override fun onCreate() {
        super.onCreate()
        SingletonHolder.init(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val playerChannel : NotificationChannel = NotificationChannel(
                CHANNEL_ID,
                "Player Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(playerChannel)
        }
    }

}