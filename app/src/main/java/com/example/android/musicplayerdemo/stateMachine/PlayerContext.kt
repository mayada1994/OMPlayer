package com.example.android.musicplayerdemo.stateMachine

import android.content.Context
import android.media.MediaPlayer
import com.example.android.musicplayerdemo.entities.TrackMetadata
import java.util.concurrent.Executor
import java.util.concurrent.ScheduledExecutorService

interface PlayerContext {
    val context: Context
    val mediaPlayer: MediaPlayer
    val playlist: MutableList<Int>

    fun updateMetadata(metadata: TrackMetadata)
}