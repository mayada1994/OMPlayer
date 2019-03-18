package com.example.android.omplayer.services.playerService

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.omplayer.consts.Extra
import com.example.android.omplayer.App.Companion.CHANNEL_ID
import com.example.android.omplayer.R
import com.example.android.omplayer.di.SingletonHolder
import com.example.android.omplayer.stateMachine.Action
import com.example.android.omplayer.utils.LibraryUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class PlayerService : Service() {

    val playerManager = SingletonHolder.playerManager
    val serviceContext = this

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

        when (intent?.getSerializableExtra(Extra.ACTION)) {
            is Action.Play -> {
                getNotification(R.drawable.pause, "pause", pausePendingIntent)
            }

            is Action.Pause -> {
                getNotification(R.drawable.play, "play", playPendingIntent)
            }
        }

        return START_NOT_STICKY
    }

    private fun getNotification(icon: Int, text: String, intent: PendingIntent) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(coroutineContext) {
                val currentTrack = LibraryUtil.tracklist[LibraryUtil.selectedTrack]
                val currentAlbum = SingletonHolder.db.albumDao().getAlbumById(currentTrack.albumId)
                val currentCover = loadImage(currentAlbum.cover)
                withContext(Dispatchers.Main) {
                    val notification: Notification = NotificationCompat.Builder(serviceContext, CHANNEL_ID)
                        .setLargeIcon(currentCover)
                        .setSmallIcon(R.drawable.music_icon)
                        .setContentTitle(currentTrack.title)
                        .setContentText(currentAlbum.title)
                        .addAction(icon, text, intent)
                        .addAction(R.drawable.next, "next", nextPendingIntent)
                        .addAction(R.drawable.stop, "stop", stopPendingIntent)
                        .build()!!
                    startForeground(1, notification)
                }
            }
        }
    }

    fun loadImage(albumArtUrl: String): Bitmap {
        try {
            val file = File(albumArtUrl)
            val uri = Uri.fromFile(file)

            return Glide.with(serviceContext)
                .asBitmap()
                .load(uri).apply(
                    RequestOptions()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                ).submit().get()
        } catch (e: Exception) {
        }
        return BitmapFactory.decodeResource(serviceContext.resources, R.drawable.placeholder)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
    }
}