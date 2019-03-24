package com.omplayer.app.services.playerService

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import androidx.media.session.MediaButtonReceiver
import com.omplayer.app.application.App.Companion.CHANNEL_ID
import com.omplayer.app.consts.Extra
import com.omplayer.app.consts.RequestCodes
import com.omplayer.app.di.SingletonHolder
import com.omplayer.app.stateMachine.Action

class PlayerService : Service() {

    private val playerManager = SingletonHolder.playerManager

    private val becomingNoisyReceiver =
        BecomingNoisyReceiver()

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

    override fun onCreate() {
        super.onCreate()
        //Handles headphones coming unplugged. cannot be done through a manifest receiver
        val filter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
        registerReceiver(becomingNoisyReceiver, filter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        MediaButtonReceiver.handleIntent(playerManager.mediaSessionCompat, intent)

        val notification: Notification

        when (intent?.getSerializableExtra(Extra.ACTION)) {
            is Action.Play -> {
                notification = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(com.omplayer.app.R.drawable.music_icon)
                    .setContentTitle("Pass here song name")
                    .setContentText("Playing")
                    .addAction(com.omplayer.app.R.drawable.prev, "prev", prevPendingIntent)
                    .addAction(com.omplayer.app.R.drawable.pause, "pause", pausePendingIntent)
                    .addAction(com.omplayer.app.R.drawable.next, "next", nextPendingIntent)
                    .addAction(com.omplayer.app.R.drawable.close, "close", stopPendingIntent)
                    .setStyle(MediaStyle())
                    .build()!!
                startForeground(1, notification)
            }

            is Action.Pause -> {
                notification = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(com.omplayer.app.R.drawable.music_icon)
                    .setContentTitle("Pass here song name")
                    .setContentText("Paused")
                    .addAction(com.omplayer.app.R.drawable.prev, "prev", prevPendingIntent)
                    .addAction(com.omplayer.app.R.drawable.play, "play", playPendingIntent)
                    .addAction(com.omplayer.app.R.drawable.next, "next", nextPendingIntent)
                    .addAction(com.omplayer.app.R.drawable.close, "close", stopPendingIntent)
                    .setStyle(MediaStyle())
                    .build()!!
                startForeground(1, notification)
            }

        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(becomingNoisyReceiver)
        stopForeground(true)
//        stopForeground(STOP_FOREGROUND_DETACH)
    }

}
