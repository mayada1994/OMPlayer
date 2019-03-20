package com.example.android.musicplayerdemo.services.playerService

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import androidx.media.session.MediaButtonReceiver
import com.example.android.musicplayerdemo.application.App.Companion.CHANNEL_ID
import com.example.android.musicplayerdemo.consts.Extra
import com.example.android.musicplayerdemo.consts.RequestCodes
import com.example.android.musicplayerdemo.di.SingletonHolder
import com.example.android.musicplayerdemo.stateMachine.Action

class PlayerService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private val playerManager = SingletonHolder.playerManager

    private val stopPendingIntent by lazy {
        PendingIntent.getBroadcast(
            this,
            RequestCodes.STOP.hashCode(),
            Intent(this, PlayerBroadcastReceiver::class.java)
                .putExtra(Extra.ACTION, Action.Stop()),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    private val playPendingIntent by lazy {
        PendingIntent.getBroadcast(
            this,
            RequestCodes.PLAY.hashCode(),
            Intent(this, PlayerBroadcastReceiver::class.java)
                .putExtra(Extra.ACTION, Action.Play()),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    private val pausePendingIntent by lazy {
        PendingIntent.getBroadcast(
            this,
            RequestCodes.PAUSE.hashCode(),
            Intent(this, PlayerBroadcastReceiver::class.java)
                .putExtra(Extra.ACTION, Action.Pause()),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    private val nextPendingIntent by lazy {
        PendingIntent.getBroadcast(
            this,
            RequestCodes.NEXT.hashCode(),
            Intent(this, PlayerBroadcastReceiver::class.java)
                .putExtra(Extra.ACTION, Action.Next()),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    private val prevPendingIntent by lazy {
        PendingIntent.getBroadcast(
            this,
            RequestCodes.PREV.hashCode(),
            Intent(this, PlayerBroadcastReceiver::class.java)
                .putExtra(Extra.ACTION, Action.Prev()),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        MediaButtonReceiver.handleIntent(playerManager.mediaSessionCompat, intent)

        val notification: Notification

        when (intent?.getSerializableExtra(Extra.ACTION)) {
            is Action.Play -> {
                notification = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(com.example.android.musicplayerdemo.R.drawable.music_icon)
                    .setContentTitle("Pass here song name")
                    .setContentText("Playing")
                    .addAction(com.example.android.musicplayerdemo.R.drawable.prev, "prev", prevPendingIntent)
                    .addAction(com.example.android.musicplayerdemo.R.drawable.pause, "pause", pausePendingIntent)
                    .addAction(com.example.android.musicplayerdemo.R.drawable.next, "next", nextPendingIntent)
                    .addAction(com.example.android.musicplayerdemo.R.drawable.stop, "stop", stopPendingIntent)
                    .setStyle(MediaStyle())
                    .build()!!
                startForeground(1, notification)
            }

            is Action.Pause -> {
                notification = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(com.example.android.musicplayerdemo.R.drawable.music_icon)
                    .setContentTitle("Pass here song name")
                    .setContentText("Paused")
                    .addAction(com.example.android.musicplayerdemo.R.drawable.play, "play", playPendingIntent)
                    .addAction(com.example.android.musicplayerdemo.R.drawable.next, "next", nextPendingIntent)
                    .addAction(com.example.android.musicplayerdemo.R.drawable.stop, "stop", stopPendingIntent)
                    .setStyle(MediaStyle())
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
