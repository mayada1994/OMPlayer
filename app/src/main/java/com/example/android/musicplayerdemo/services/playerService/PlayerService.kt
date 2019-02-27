package com.example.android.musicplayerdemo.services.playerService

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.android.musicplayerdemo.App.Companion.CHANNEL_ID
import com.example.android.musicplayerdemo.R
import com.example.android.musicplayerdemo.consts.Extra
import com.example.android.musicplayerdemo.di.SingletonHolder

class PlayerService : Service() {

    val playerManager = SingletonHolder.playerManager


    override fun onBind(intent: Intent?): IBinder? {
        TODO("not impelemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        val playPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            Intent(this, PlayerBroadcastReciever::class.java)
                    .putExtra(Extra.ACTION, Extra.PLAY),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val pausePendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            Intent(this, BroadcastReceiver::class.java)
                .putExtra(Extra.ACTION,Extra.PAUSE),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val nextPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            Intent(this, BroadcastReceiver::class.java)
                .putExtra(Extra.ACTION,Extra.NEXT),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val stopPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            Intent(this, BroadcastReceiver::class.java)
                .putExtra(Extra.ACTION,Extra.STOP),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification: Notification

        when (intent?.getStringExtra(Extra.ACTION)) {
            Extra.PLAY -> {
                notification = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.music_icon)
                    .setContentTitle("Player Service")
                    .setContentText("Yey! It works!")
                    .addAction(R.drawable.play, "play", playPendingIntent)
                    .addAction(R.drawable.next,"next",nextPendingIntent)
                    .addAction(R.drawable.stop,"stop",stopPendingIntent)
                    .build()!!
                startForeground(1, notification)
            }

            Extra.PAUSE -> {
                notification = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.music_icon)
                    .setContentTitle("Player Service")
                    .setContentText("Yey! It works!")
                    .addAction(R.drawable.pause, "pause", pausePendingIntent)
                    .addAction(R.drawable.next,"next",nextPendingIntent)
                    .addAction(R.drawable.stop,"stop",stopPendingIntent)
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