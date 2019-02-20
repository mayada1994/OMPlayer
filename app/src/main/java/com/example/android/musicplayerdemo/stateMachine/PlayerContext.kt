package com.example.android.musicplayerdemo.stateMachine

import android.content.Context
import android.media.MediaPlayer
import java.util.concurrent.Executor
import java.util.concurrent.ScheduledExecutorService

interface PlayerContext {
    val context: Context
    val mediaPlayer: MediaPlayer?
    val playlist: MutableList<Int>
    val callback: SeekbarCallback
    var executor: ScheduledExecutorService?
    var seekbarPositionUpdateTask: Runnable?
}