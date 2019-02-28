package com.example.android.musicplayerdemo.services.playerService

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.android.musicplayerdemo.application.App.Companion.CHANNEL_ID
import com.example.android.musicplayerdemo.R
import com.example.android.musicplayerdemo.consts.Extra
import com.example.android.musicplayerdemo.consts.RequestCodes
import com.example.android.musicplayerdemo.di.SingletonHolder
import com.example.android.musicplayerdemo.stateMachine.Action

class PlayerService : Service() {

    val playerManager = SingletonHolder.playerManager

    val stopPendingIntent by lazy {
        PendingIntent.getBroadcast(
            this,
            RequestCodes.STOP.hashCode(),
            Intent(this, PlayerBroadcastReceiver::class.java)
                .putExtra(Extra.ACTION, Action.Stop()),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    val playPendingIntent by lazy {
        PendingIntent.getBroadcast(
            this,
            RequestCodes.PLAY.hashCode(),
            Intent(this, PlayerBroadcastReceiver::class.java)
                .putExtra(Extra.ACTION, Action.Play()),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    val pausePendingIntent by lazy {
        PendingIntent.getBroadcast(
            this,
            RequestCodes.PAUSE.hashCode(),
            Intent(this, PlayerBroadcastReceiver::class.java)
                .putExtra(Extra.ACTION, Action.Pause()),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    val nextPendingIntent by lazy {
        PendingIntent.getBroadcast(
            this,
            RequestCodes.NEXT.hashCode(),
            Intent(this, PlayerBroadcastReceiver::class.java)
                .putExtra(Extra.ACTION, Action.Next()),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    val prevPendingIntent by lazy {
        PendingIntent.getBroadcast(
            this,
            RequestCodes.PREV.hashCode(),
            Intent(this, PlayerBroadcastReceiver::class.java)
                .putExtra(Extra.ACTION, Action.Prev()),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }


    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val notification: Notification

        when (intent?.getSerializableExtra(Extra.ACTION)) {
            is Action.Play -> {
                notification = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.music_icon)
                    .setContentTitle("Pass here song name")
                    .setContentText("Playing")
                    .addAction(R.drawable.pause, "pause", pausePendingIntent)
                    .addAction(R.drawable.next, "next", nextPendingIntent)
                    .addAction(R.drawable.stop, "stop", stopPendingIntent)
                    .build()!!
                startForeground(1, notification)
            }

            is Action.Pause -> {
                notification = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.music_icon)
                    .setContentTitle("Pass here song name")
                    .setContentText("Paused")
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
        stopForeground(STOP_FOREGROUND_DETACH)
    }

}
