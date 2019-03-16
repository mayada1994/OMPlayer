package com.example.android.omplayer.services.playerService

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.android.omplayer.consts.Extra
import com.example.android.omplayer.App.Companion.CHANNEL_ID
import com.example.android.omplayer.R
import com.example.android.omplayer.di.SingletonHolder
import com.example.android.omplayer.stateMachine.Action


class PlayerService : Service() {

    val playerManager = SingletonHolder.playerManager

    val stopPendingIntent by lazy {
        PendingIntent.getBroadcast(
            this,
            Extra.STOP.hashCode(),
            Intent(this, PlayerBroadcastReceiver::class.java)
                .putExtra(Extra.ACTION, Action.Stop()),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    val playPendingIntent by lazy {
        PendingIntent.getBroadcast(
            this,
            1,
            Intent(this, PlayerBroadcastReceiver::class.java)
                .putExtra(Extra.ACTION, Action.Play()),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    val pausePendingIntent by lazy {
        PendingIntent.getBroadcast(
            this,
            2,
            Intent(this, PlayerBroadcastReceiver::class.java)
                .putExtra(Extra.ACTION, Action.Pause()),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    val nextPendingIntent by lazy {
        PendingIntent.getBroadcast(
            this,
            3,
            Intent(this, PlayerBroadcastReceiver::class.java)
                .putExtra(Extra.ACTION, Action.Next()),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }


    override fun onBind(intent: Intent?): IBinder? {
        TODO("not impelemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val notification: Notification

        when (intent?.getSerializableExtra(Extra.ACTION)) {
            is Action.Play -> {
                notification = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.music_icon)
                    .setContentTitle("Player Service")
                    .setContentText("Yey! It works!")
                    .addAction(R.drawable.pause, "pause", pausePendingIntent)
                    .addAction(R.drawable.next, "next", nextPendingIntent)
                    .addAction(R.drawable.stop, "stop", stopPendingIntent)
                    .build()!!
                startForeground(1, notification)
            }

            is Action.Pause -> {
                notification = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.music_icon)
                    .setContentTitle("Player Service")
                    .setContentText("Yey! It works!")
                    .addAction(R.drawable.play, "play", playPendingIntent)
                    .addAction(R.drawable.next, "next", nextPendingIntent)
                    .addAction(R.drawable.stop, "stop", stopPendingIntent)
                    .build()!!
                startForeground(1, notification)
            }

        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
    }
}
