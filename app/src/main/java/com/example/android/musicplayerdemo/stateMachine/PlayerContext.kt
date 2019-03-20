package com.example.android.musicplayerdemo.stateMachine

import android.content.Context
import android.media.MediaPlayer
import android.support.v4.media.session.MediaSessionCompat
import com.example.android.musicplayerdemo.entities.TrackMetadata
import java.util.concurrent.Executor
import java.util.concurrent.ScheduledExecutorService

interface PlayerContext {
    val context: Context
    val mediaPlayer: MediaPlayer
    val playlist: MutableList<Int>
    val mediaSessionCompat : MediaSessionCompat

    fun updateMetadata(metadata: TrackMetadata)
}